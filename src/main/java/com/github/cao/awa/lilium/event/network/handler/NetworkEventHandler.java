package com.github.cao.awa.lilium.event.network.handler;

import com.github.cao.awa.kalmia.annotations.inaction.DoNotOverride;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.event.handler.EventHandler;
import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;

public interface NetworkEventHandler<P extends Packet<?>, E extends NetworkEvent<P>> extends EventHandler<E> {
    void handle(RequestRouter router, P packet);

    @Override
    @DoNotOverride
    default void handle(E event) {
        RequestRouter router = event.router();
        P packet = event.packet();

        handle(
                router,
                packet
        );

        LiliumEnv.awaitManager.notice(packet.receipt());
    }
}
