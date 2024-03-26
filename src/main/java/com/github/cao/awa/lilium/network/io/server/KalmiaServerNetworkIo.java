package com.github.cao.awa.lilium.network.io.server;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.apricot.thread.pool.ExecutorFactor;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.lilium.config.bootstrap.LiliumNetworkConfig;
import com.github.cao.awa.lilium.network.io.server.channel.LiliumServerChannelInitializer;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.server.LiliumServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Network io of kalmia server.
 *
 * @author 草二号机
 * @author cao_awa
 * @since 1.0.0
 */
@Stable
public class KalmiaServerNetworkIo {
    private static final Logger LOGGER = LogManager.getLogger("LiliumServerNetworkIo");
    private static final Supplier<NioEventLoopGroup> DEFAULT_CHANNEL = () -> new NioEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );
    private static final Supplier<EpollEventLoopGroup> EPOLL_CHANNEL = () -> new EpollEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );

    private final LiliumServerChannelInitializer channelInitializer;
    private final LiliumServer server;
    private ChannelFuture channelFuture;
    private final List<RequestRouter> connections = Collections.synchronizedList(ApricotCollectionFactor.arrayList());
    private final Map<LongAndExtraIdentity, List<RequestRouter>> routers = ApricotCollectionFactor.hashMap();

    public KalmiaServerNetworkIo(LiliumServer server) {
        this.server = server;
        this.channelInitializer = new LiliumServerChannelInitializer(server).subscribe(this.connections);
    }

    public List<RequestRouter> getRouter(LongAndExtraIdentity accessIdentity) {
        return this.routers.get(accessIdentity);
    }

    public void login(LongAndExtraIdentity accessIdentity, RequestRouter router) {
        this.routers.compute(
                        accessIdentity,
                        (k, v) -> v == null ? ApricotCollectionFactor.arrayList() : v
                )
                .add(router);
        LOGGER.info("Login '{}': {}",
                accessIdentity,
                router.metadata()
                        .formatConnectionId()
        );
    }

    public void logout(LongAndExtraIdentity accessIdentity, RequestRouter router) {
        List<RequestRouter> routers = this.routers.get(accessIdentity);
        if (routers != null) {
            routers.remove(router);

            LOGGER.info("Logout '{}': {}",
                    accessIdentity,
                    router.metadata()
                            .formatConnectionId()
            );
        }
    }

    public void start() throws InterruptedException {
        LiliumNetworkConfig networkConfig = this.server
                .config.get()
                .network.get();
        boolean expectEpoll = networkConfig.useEpoll.get();
        boolean epoll = Epoll.isAvailable();

        LOGGER.info(expectEpoll ?
                epoll ?
                        "Kalmia network io using Epoll" :
                        "Kalmia network io expected Epoll, but Epoll is not available, switch to NIO" :
                "Kalmia network io using NIO"
        );

        LOGGER.info("Server open on {}:{}",
                networkConfig.bindHost.get(),
                networkConfig.bindPort.get()
        );

        Supplier<? extends EventLoopGroup> lazy = epoll ? EPOLL_CHANNEL : DEFAULT_CHANNEL;

        Class<? extends ServerSocketChannel> channel = epoll ?
                EpollServerSocketChannel.class :
                NioServerSocketChannel.class;

        EventLoopGroup boss = lazy.get();
        EventLoopGroup worker = lazy.get();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            this.channelFuture = bootstrap.channel(channel)
                    .group(
                            boss,
                            worker
                    )
                    .option(
                            ChannelOption.SO_BACKLOG,
                            256
                    )
                    .childOption(
                            // Real-time response is necessary
                            // Enable TCP no delay to improve response speeds
                            ChannelOption.TCP_NODELAY,
                            true
                    )
                    .childHandler(this.channelInitializer)
                    .bind(
                            networkConfig.bindHost.get(),
                            networkConfig.bindPort.get()
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
            this.connections.forEach(RequestRouter::disconnect);
            this.channelFuture.channel()
                    .close()
                    .sync();
        } catch (InterruptedException interruptedException) {
            LOGGER.error("Interrupted whilst closing channel");
        }
    }
}