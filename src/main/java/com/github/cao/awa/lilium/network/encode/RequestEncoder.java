package com.github.cao.awa.lilium.network.encode;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RequestEncoder extends MessageToByteEncoder<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger("RequestEncoder");
    private final RequestRouter router;

    public RequestEncoder(RequestRouter router) {
        this.router = router;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<?> request, ByteBuf out) throws Exception {
        // Encode it by router.
        byte[] payload = BytesUtil.concat(request.encode(this.router));

        // Mark the length for frame reading.
        byte[] lengthMark = Base256.intToBuf(payload.length);

        // Write packet length and payload data
        out.writeBytes(lengthMark);
        out.writeBytes(payload);
    }
}
