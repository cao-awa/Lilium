package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.handshake;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.AutoEventHandler;
import com.github.cao.awa.lilium.event.network.handler.NetworkEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handshake.HandshakeAesEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ClientHelloEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.handshake.HandshakeAesPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;

@Auto
@AutoEventHandler(HandshakeAesEvent.class)
public interface HandshakeAesEventHandler extends NetworkEventHandler<HandshakeAesPacket, HandshakeAesEvent> {
}
