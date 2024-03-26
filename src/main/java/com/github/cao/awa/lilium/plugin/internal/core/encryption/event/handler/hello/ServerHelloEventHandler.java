package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.AutoEventHandler;
import com.github.cao.awa.lilium.event.network.handler.NetworkEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ClientHelloEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ServerHelloEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ServerHelloPacket;

@Auto
@AutoEventHandler(ServerHelloEvent.class)
public interface ServerHelloEventHandler extends NetworkEventHandler<ServerHelloPacket, ServerHelloEvent> {
}
