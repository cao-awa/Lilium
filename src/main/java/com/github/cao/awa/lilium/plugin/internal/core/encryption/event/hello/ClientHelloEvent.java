package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello;

import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;

public class ClientHelloEvent extends NetworkEvent<ClientHelloPacket> {
    public ClientHelloEvent(RequestRouter router, ClientHelloPacket packet) {
        super(router, packet);
    }
}
