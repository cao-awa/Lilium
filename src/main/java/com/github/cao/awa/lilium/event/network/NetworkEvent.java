package com.github.cao.awa.lilium.event.network;

import com.github.cao.awa.lilium.event.Event;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;

public class NetworkEvent<T extends Packet<?>> extends Event {
    private final RequestRouter router;
    private final T packet;

    public NetworkEvent(RequestRouter router, T packet) {
        this.router = router;
        this.packet = packet;
    }

    public RequestRouter router() {
        return this.router;
    }

    public T packet() {
        return this.packet;
    }
}
