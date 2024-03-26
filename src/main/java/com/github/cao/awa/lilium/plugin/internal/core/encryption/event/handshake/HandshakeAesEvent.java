package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handshake;

import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.handshake.HandshakeAesPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;

public class HandshakeAesEvent extends NetworkEvent<HandshakeAesPacket> {
    public HandshakeAesEvent(RequestRouter router, HandshakeAesPacket packet) {
        super(router, packet);
    }
}
