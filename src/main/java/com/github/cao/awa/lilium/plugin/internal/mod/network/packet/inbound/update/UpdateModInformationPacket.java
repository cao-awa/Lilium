package com.github.cao.awa.lilium.plugin.internal.mod.network.packet.inbound.update;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.lilium.annotation.auto.network.event.NetworkEventTarget;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.lilium.network.PacketIdentifier;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.lilium.network.packet.handler.stateless.StatelessHandler;
import com.github.cao.awa.lilium.plugin.internal.mod.network.event.update.UpdateModInformationEvent;

@AutoSolvedPacket(crypto = false)
@NetworkEventTarget(UpdateModInformationEvent.class)
public class UpdateModInformationPacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.UPDATE_MOD_INFORMATION;
    @AutoData
    @DoNotSet
    private String modName;

    @Auto
    public UpdateModInformationPacket(BytesReader reader) {
        super(reader);
    }

    public UpdateModInformationPacket(String modName) {
        this.modName = modName;
    }

    @Getter
    public String modName() {
        return this.modName;
    }
}
