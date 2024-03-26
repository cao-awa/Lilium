package com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.AutoEventHandler;
import com.github.cao.awa.lilium.event.network.handler.NetworkEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ClientHelloEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;
import com.github.cao.awa.lilium.plugin.internal.mod.network.event.update.UpdateModInformationEvent;
import com.github.cao.awa.lilium.plugin.internal.mod.network.packet.inbound.update.UpdateModInformationPacket;

@Auto
@AutoEventHandler(ClientHelloEvent.class)
public interface ClientHelloEventHandler extends NetworkEventHandler<ClientHelloPacket, ClientHelloEvent> {
}
