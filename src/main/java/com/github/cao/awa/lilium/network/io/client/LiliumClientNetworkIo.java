package com.github.cao.awa.lilium.network.io.client;

import com.github.cao.awa.apricot.thread.pool.ExecutorFactor;
import com.github.cao.awa.lilium.client.LiliumClient;
import com.github.cao.awa.lilium.config.client.LiliumClientConfig;
import com.github.cao.awa.lilium.network.io.client.channel.LiliumClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Network io of kalmia client.
 *
 * @author 草二号机
 * @author cao_awa
 * @since 1.0.0
 */
public class LiliumClientNetworkIo {
    private static final Logger LOGGER = LogManager.getLogger("LiliumClientNetworkIo");
    private static final Supplier<NioEventLoopGroup> DEFAULT_CHANNEL = () -> new NioEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );
    private static final Supplier<EpollEventLoopGroup> EPOLL_CHANNEL = () -> new EpollEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );

    private final LiliumClientChannelInitializer channelInitializer;
    private ChannelFuture channelFuture;
    private final LiliumClient client;

    public LiliumClientNetworkIo(LiliumClient client) {
        this.channelInitializer = new LiliumClientChannelInitializer(client);
        this.client = client;
    }

    public void connect() throws Exception {
        LiliumClientConfig config = this.client.config.get();
        boolean expectEpoll = config.useEpoll.get();
        boolean epoll = Epoll.isAvailable();

        LOGGER.info(expectEpoll ?
                            epoll ?
                                    "Kalmia network io using Epoll" :
                                    "Kalmia network io expected Epoll, but Epoll is not available, switch to NIO" :
                            "Kalmia network io using NIO");

        Supplier<? extends EventLoopGroup> lazy = epoll ? EPOLL_CHANNEL : DEFAULT_CHANNEL;

        Class<? extends SocketChannel> channel = epoll ?
                EpollSocketChannel.class :
                NioSocketChannel.class;

        EventLoopGroup boss = lazy.get();
        EventLoopGroup worker = lazy.get();
        Bootstrap bootstrap = new Bootstrap();
        try {
            this.channelFuture = bootstrap.channel(channel)
                                          .group(
                                                  boss
                                          )
                                          .option(
                                                  // Real-time response is necessary
                                                  // Enable TCP no delay to improve response speeds
                                                  ChannelOption.TCP_NODELAY,
                                                  true
                                          )
                                          .handler(this.channelInitializer)
                                          .connect(
                                                  config.serverHost.get(),
                                                  config.serverPort.get()
                                          )
                                          .syncUninterruptibly()
                                          .channel()
                                          .closeFuture()
                                          .sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void shutdown() {
        try {
            this.channelFuture.channel()
                              .close()
                              .sync();
        } catch (InterruptedException interruptedException) {
            LOGGER.error("Interrupted whilst closing channel");
        }
    }
}