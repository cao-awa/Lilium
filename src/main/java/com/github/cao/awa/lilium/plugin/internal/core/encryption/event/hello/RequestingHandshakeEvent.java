package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello;

import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.RequestingHandshakePacket;

public class RequestingHandshakeEvent extends NetworkEvent<RequestingHandshakePacket> {
    public RequestingHandshakeEvent(RequestRouter router, RequestingHandshakePacket packet) {
        super(router, packet);
    }
}
