package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello;

import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ServerHelloPacket;

public class ServerHelloEvent extends NetworkEvent<ServerHelloPacket> {
    public ServerHelloEvent(RequestRouter router, ServerHelloPacket packet) {
        super(router, packet);
    }
}
