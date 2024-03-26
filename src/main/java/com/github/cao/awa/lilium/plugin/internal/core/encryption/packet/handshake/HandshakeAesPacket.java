package com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.handshake;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.lilium.annotation.auto.network.event.NetworkEventTarget;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.lilium.network.PacketIdentifier;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.stateless.StatelessHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handshake.HandshakeAesEvent;

@NetworkEventTarget(HandshakeAesEvent.class)
@AutoSolvedPacket(crypto = false, stateless = true)
public class HandshakeAesPacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.HANDSHAKE_AES;
    @AutoData
    private byte[] aesKey;

    @Auto
    public HandshakeAesPacket(BytesReader reader) {
        super(reader);
    }

    public HandshakeAesPacket(byte[] aesKey) {
        this.aesKey = aesKey;
    }

    @Getter
    public byte[] aesKey() {
        return this.aesKey;
    }
}
