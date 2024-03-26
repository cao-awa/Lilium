package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.AutoEventHandler;
import com.github.cao.awa.lilium.event.network.handler.NetworkEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ClientHelloEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.RequestingHandshakeEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.RequestingHandshakePacket;

@Auto
@AutoEventHandler(RequestingHandshakeEvent.class)
public interface RequestingHandshakeEventHandler extends NetworkEventHandler<RequestingHandshakePacket, RequestingHandshakeEvent> {
}
