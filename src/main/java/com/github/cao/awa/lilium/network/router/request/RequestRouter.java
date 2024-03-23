package com.github.cao.awa.lilium.network.router.request;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.thread.pool.ExecutorFactor;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.network.router.RequestRouterConfig;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.exception.network.invalid.InvalidPacketException;
import com.github.cao.awa.lilium.function.provider.Consumers;
import com.github.cao.awa.lilium.network.encode.compress.RequestCompressor;
import com.github.cao.awa.lilium.network.encode.compress.RequestCompressorType;
import com.github.cao.awa.lilium.network.encode.crypto.CryptoTransportLayer;
import com.github.cao.awa.lilium.network.encode.crypto.TransportLayerCrypto;
import com.github.cao.awa.lilium.network.encode.crypto.symmetric.no.NoCrypto;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.UnsolvedPacket;
import com.github.cao.awa.lilium.network.packet.handler.PacketHandler;
import com.github.cao.awa.lilium.network.packet.handler.handshake.HandshakeHandler;
import com.github.cao.awa.lilium.network.packet.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.lilium.network.packet.handler.login.LoginHandler;
import com.github.cao.awa.lilium.network.packet.handler.stateless.StatelessHandler;
import com.github.cao.awa.lilium.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.lilium.network.router.NetworkRouter;
import com.github.cao.awa.lilium.network.router.request.meta.RequestRouterMetadata;
import com.github.cao.awa.lilium.network.router.request.status.RequestState;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.affair.Affair;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class RequestRouter extends NetworkRouter<UnsolvedPacket<?>> {
    private static final Logger LOGGER = LogManager.getLogger("RequestRouter");
    private final ExecutorService executor = ExecutorFactor.intensiveIo();
    private final Map<RequestState, PacketHandler<?>> handlers = EntrustEnvironment.operation(ApricotCollectionFactor.hashMap(),
            handlers -> {
                handlers.put(RequestState.HELLO,
                        new HandshakeHandler()
                );
                handlers.put(RequestState.AUTH,
                        new LoginHandler()
                );
                handlers.put(RequestState.AUTHED,
                        new AuthedRequestHandler()
                );
            }
    );
    private final CryptoTransportLayer transportLayer = new CryptoTransportLayer();
    private RequestState states;
    private PacketHandler<?> allowedHandler;
    private final StatelessHandler statelessHandler = new StatelessHandler();
    private ChannelHandlerContext context;
    private final Consumer<RequestRouter> activeCallback;
    private final RequestCompressor compressor = new RequestCompressor();
    private final Affair funeral = Affair.empty();
    private LongAndExtraIdentity accessIdentity;

    private final RequestRouterMetadata metadata = RequestRouterMetadata.create();
    @AutoConfig
    public final ConfigEntry<RequestRouterConfig> config = ConfigEntry.entry();

    public RequestRouter() {
        this(Consumers.doNothing());
    }

    public RequestRouter(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        setStates(RequestState.HELLO);
        LiliumEnv.CONFIG_FRAMEWORK.createConfig(this);
    }

    public LongAndExtraIdentity accessIdentity() {
        return this.accessIdentity;
    }

    public void accessIdentity(LongAndExtraIdentity identity) {
        this.accessIdentity = identity;
    }

    public RequestCompressor getCompressor() {
        return this.compressor;
    }

    public void setCompressor(RequestCompressorType type) {
        this.compressor.setCompressor(type);
    }

    public RequestState getStates() {
        return this.states;
    }

    public void setStates(RequestState states) {
        this.states = states;
        this.allowedHandler = this.handlers.get(states);
    }

    public ExecutorService executor() {
        return this.executor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnsolvedPacket<?> msg) throws Exception {
        try {
            if (msg.requireCrypto()) {
                if (this.transportLayer.crypto() instanceof NoCrypto) {
                    send(new OperationInvalidPacket("packet.crypto.required").receipt(msg.receipt()));
                    return;
                }
            }

            if (msg.isStateless()) {
                this.statelessHandler.tryInbound(msg,
                        this
                );
            } else {
                this.allowedHandler.tryInbound(msg,
                        this
                );
            }
        } catch (InvalidPacketException e) {
            // TODO
            e.printStackTrace();

            send(new OperationInvalidPacket("server.internal.error").receipt(msg.receipt()));
        } catch (Exception e) {
            // TODO
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException) {
            disconnect();
        } else {
            LOGGER.error("Unhandled exception",
                    cause
            );
        }
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.context = ctx;
        this.activeCallback.accept(this);
        this.context.channel()
                .closeFuture()
                .addListener(this :: disconnect);
    }

    public void disconnect() {
        this.context.disconnect();
    }

    public void disconnect(Future<? super Void> future) {
        this.funeral.done();
    }

    public boolean isOpen() {
        return this.context.channel()
                .isOpen();
    }

    public RequestRouter funeral(Runnable action) {
        this.funeral.add(action);
        return this;
    }

    public RequestRouter funeral(Consumer<RequestRouter> action) {
        this.funeral.add(() -> action.accept(this));
        return this;
    }

    public byte[] decode(byte[] cipherText) {
        // Decode the data with transport layer.
        byte[] decodeResult = this.transportLayer.decode(cipherText);

        BytesReader reader = BytesReader.of(decodeResult);

        // Decoded data included compress mark, used to decompress.
        int compressId = Base256.tagFromBuf(reader.read(2));

        // Decompress the packet data.
        return RequestCompressorType.TYPES.get(compressId)
                .compressor()
                .decompress(reader.all());
    }

    public byte[] encode(byte[] sourceData) {
        RequestCompressor compressor = getCompressor();

        // Compress the packet data.
        int compressId;

        byte[] compressResult;

        // Do not compress when data smaller than specially bytes, default is 1423.
        if (sourceData.length < this.config.get().compressThreshold.get()) {
            compressId = RequestCompressorType.NONE.id();
            compressResult = sourceData;
        } else {
            LOGGER.debug("Trying compress with {}",
                    RequestCompressorType.TYPES.get(compressor.id())
            );

            compressId = compressor.id();
            compressResult = compressor.compress(sourceData);

            // If data length is not reduced, then do not use the compress result.
            if (sourceData.length > compressResult.length) {
                // Success to compress, use the compressed result.
                LOGGER.debug("Success to compress: {}(source) > {}(compressed)",
                        sourceData.length,
                        compressResult.length
                );
            } else {
                LOGGER.debug("Failed to compress: {}(source) <= {}(compressed)",
                        sourceData.length,
                        compressResult.length
                );

                // Unable to compress, use the source.
                compressId = RequestCompressorType.NONE.id();
                compressResult = sourceData;
            }
        }

        // Encode the data with transport layer.
        return this.transportLayer.encode(
                BytesUtil.concat(
                        // Include the compress mark in the encoded data.
                        Base256.tagToBuf(compressId),
                        compressResult
                )
        );
    }

    public boolean isCipherEquals(byte[] cipher) {
        return this.transportLayer.isCipherEquals(cipher);
    }

    public void send(Packet<?> packet) {
        this.context.channel()
                .eventLoop()
                .execute(() -> {
                    this.context.writeAndFlush(packet);
                });
    }

    public void sendImmediately(Packet<?> packet) {
        this.context.writeAndFlush(packet);
    }

    public void setCrypto(TransportLayerCrypto crypto) {
        this.transportLayer.setCrypto(crypto);
    }

    public void setIv(byte[] iv) {
        this.transportLayer.setIv(iv);
    }

    public PacketHandler<?> getHandler() {
        return this.handlers.get(this.states);
    }

    public RequestRouterMetadata metadata() {
        return this.metadata;
    }
}
