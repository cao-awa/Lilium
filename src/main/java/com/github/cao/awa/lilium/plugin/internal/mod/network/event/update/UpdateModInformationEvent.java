package com.github.cao.awa.lilium.plugin.internal.mod.network.event.update;

import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.mod.network.packet.inbound.update.UpdateModInformationPacket;

public class UpdateModInformationEvent extends NetworkEvent<UpdateModInformationPacket> {
    public UpdateModInformationEvent(RequestRouter router, UpdateModInformationPacket packet) {
        super(router, packet);
    }
}
