package com.github.cao.awa.lilium.network.packet.inbound.disconnet;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.lilium.network.PacketIdentifier;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.stateless.StatelessHandler;

@AutoSolvedPacket(crypto = false, stateless = true)
public class TryDisconnectPacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.TRY_DISCONNECT;

    @AutoData
    @DoNotSet
    private String reason;

    public TryDisconnectPacket(String reason) {
        this.reason = reason;
    }

    @Auto
    public TryDisconnectPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public String reason() {
        return this.reason;
    }
}
