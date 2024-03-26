package com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.lilium.annotation.auto.network.event.NetworkEventTarget;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.lilium.network.PacketIdentifier;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.stateless.StatelessHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ClientHelloEvent;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.RequestingHandshakeEvent;

@NetworkEventTarget(RequestingHandshakeEvent.class)
@AutoSolvedPacket(crypto = false, stateless = true)
public class RequestingHandshakePacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.REQUESTING_HANDSHAKE;
    @AutoData
    private String usedSignatureKey;

    public RequestingHandshakePacket(BytesReader reader) {
        super(reader);
    }

    public RequestingHandshakePacket(String usedSignatureKey) {
        this.usedSignatureKey = usedSignatureKey;
    }

    @Getter
    public String usedSignatureKey() {
        return this.usedSignatureKey;
    }
}
