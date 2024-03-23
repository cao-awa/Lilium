package com.github.cao.awa.lilium.plugin.internal.mod.network.event.handler.update;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.AutoEventHandler;
import com.github.cao.awa.lilium.event.network.handler.NetworkEventHandler;
import com.github.cao.awa.lilium.plugin.internal.mod.network.event.update.UpdateModInformationEvent;
import com.github.cao.awa.lilium.plugin.internal.mod.network.packet.inbound.update.UpdateModInformationPacket;

@Auto
@AutoEventHandler(UpdateModInformationEvent.class)
public interface UpdateModInformationEventHandler extends NetworkEventHandler<UpdateModInformationPacket, UpdateModInformationEvent> {
}
