package com.github.cao.awa.lilium.network.encode;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.attack.replay.ReplayAttack;
import com.github.cao.awa.lilium.exception.network.attck.ReplayAttackException;
import com.github.cao.awa.lilium.exception.network.invalid.InvalidPacketException;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.lilium.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LogManager.getLogger("RequestDecoder");
    private final RequestRouter router;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private long lengthMarker = 0;
    private long currentLength = 0;

    public RequestDecoder(RequestRouter router) {
        this.router = router;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (in.readableBytes() > 0) {
                int dataLength;
                // 长度标记为0则意味着这是某个数据包的第一个帧
                // 需要更新长度标记
                if (this.lengthMarker == 0) {
                    // 读取长度标记
                    byte[] lengthMarker = new byte[4];
                    in.readBytes(lengthMarker);

                    // 解码长度标记
                    dataLength = Base256.intFromBuf(lengthMarker);

                    // 设置长度标记
                    this.lengthMarker = dataLength;

                    // 重新设置长度标记
                    dataLength = Math.min(in.readableBytes(),
                            dataLength
                    );
                } else {
                    // 此帧能读取的长度
                    dataLength = in.readableBytes();
                }

                // 读取数据...
                byte[] data = new byte[dataLength];
                in.readBytes(data);

                // Write to buffer and update current length.
                this.output.write(data);

                this.currentLength += data.length;

                // 当长度和标记一致时结束读取，开始处理数据包
                if (this.currentLength == this.lengthMarker) {
                    // 处理它......
                    done(this.output.toByteArray(),
                            out
                    );

                    // 重置并准备读取下一个数据包
                    this.output.reset();
                    this.lengthMarker = 0;
                    this.currentLength = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void done(byte[] payload, List<Object> out) {
        // 构造reader
        BytesReader reader = BytesReader.of(payload);

        // 先使用RequestRouter解码
        reader.reset(this.router.decode(reader.all()));

        byte[] digest = reader.read(reader.read());

        // 读取整个数据包用来摘要
        reader.flag();
        byte[] remain = reader.all();
        reader.back();

        // 数据摘要，如果不一直则说明可能传递出错或被篡改
        if (!Arrays.equals(MessageDigger.digestToBytes(remain,
                        MessageDigger.Sha3.SHA_512
                ),
                digest
        )) {
            throw new InvalidPacketException("Packet are not completed");
        }

        // 重放标记和时间戳，用于抵抗重放攻击
        byte[] replayMark = reader.read(16);

        // 读取时间戳
        long timestamp = SkippedBase256.readLong(reader);

        // 检查数据包标识，保证此数据包没有被重放攻击
        if (!ReplayAttack.validate(replayMark,
                timestamp
        )) {
            this.router.send(new TryDisconnectPacket("You are doing replay attack"));

            this.router.disconnect();

            throw new ReplayAttackException();
        }

        // 读取数据包ID用以确定是哪个数据包
        long id = SkippedBase256.readLong(reader);

        // 读取回执信息
        byte[] receipt = Packet.readReceipt(reader);

        // 创建未解决的数据包，待会会在RequestRouter中创建真正的数据包实例
        out.add(UnsolvedPacketFactor.create(id,
                reader.all(),
                receipt
        ));
    }
}