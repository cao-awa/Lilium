package com.github.cao.awa.lilium.network.io.server.channel;

import com.github.cao.awa.kalmia.constant.IntegerConstants;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.network.encode.RequestDecoder;
import com.github.cao.awa.lilium.network.encode.RequestEncoder;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.server.LiliumServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LiliumServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger LOGGER = LogManager.getLogger("LiliumServerChannelInitializer");
    private final LiliumServer server;
    private List<RequestRouter> subscriber;

    public LiliumServerChannelInitializer(LiliumServer server) {
        this.server = server;
    }

    public LiliumServer server() {
        return this.server;
    }

    public LiliumServerChannelInitializer subscribe(List<RequestRouter> routers) {
        this.subscriber = routers;
        return this;
    }

    public LiliumServerChannelInitializer active(RequestRouter router) {
        if (this.subscriber != null) {
            this.subscriber.add(router);
            LOGGER.info(
                    "Active connection for {}, current count: {}",
                    router.metadata()
                          .formatConnectionId(),
                    this.subscriber.size()
            );
        }
        return this;
    }

    public LiliumServerChannelInitializer unsubscribe() {
        this.subscriber = null;
        return this;
    }

    public LiliumServerChannelInitializer inactive(RequestRouter router) {
        if (this.subscriber != null) {
            this.subscriber.remove(router);
            LOGGER.info(
                    "Inactive connection for {}, current count: {}",
                    router.metadata()
                          .formatConnectionId(),
                    this.subscriber.size()
            );
        }
        return this;
    }

    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.config()
                .setRecvByteBufAllocator(new FixedRecvByteBufAllocator(IntegerConstants.K_16));
        ChannelPipeline pipeline = ch.pipeline();
        // Do decode.
        RequestRouter router = new RequestRouter().funeral(this :: inactive);
        pipeline.addLast(new RequestDecoder(router));
        pipeline.addLast(new RequestEncoder(router));
        // Do handle.
        pipeline.addLast(router);

        // Do final handle.
        router.funeral(LiliumEnv.SERVER :: logout);

        // Add to subscriber list.
        active(router);
    }
}
