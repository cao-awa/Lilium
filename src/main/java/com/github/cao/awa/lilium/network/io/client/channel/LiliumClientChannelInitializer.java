package com.github.cao.awa.lilium.network.io.client.channel;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.kalmia.constant.IntegerConstants;
import com.github.cao.awa.lilium.client.LiliumClient;
import com.github.cao.awa.lilium.network.encode.RequestDecoder;
import com.github.cao.awa.lilium.network.encode.RequestEncoder;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel initializer of kalmia client network.
 *
 * @author 草二号机
 * @since 1.0.0
 */
@Stable
public class LiliumClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final LiliumClient client;

    public LiliumClientChannelInitializer(LiliumClient client) {
        this.client = client;
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
        RequestRouter router = new RequestRouter(this.client.activeCallback());
        this.client.router(router);
        pipeline.addLast(new RequestDecoder(router));
        pipeline.addLast(new RequestEncoder(router));
        // Do handle.
        pipeline.addLast(router);
    }
}
