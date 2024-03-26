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
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@NetworkEventTarget(ClientHelloEvent.class)
@AutoSolvedPacket(crypto = false, stateless = true)
public class ClientHelloPacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.CLIENT_HELLO;
    @AutoData
    private String clientName;
    @AutoData
    private String clientVersion;
    @AutoData
    private String expectedSignatureKey;
    @AutoData
    private long usedSignatureKey;

    @Auto
    @Server
    public ClientHelloPacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public ClientHelloPacket(String clientName, String clientVersion, String expectedSignatureKey, long usedSignatureKey) {
        this.clientName = clientName;
        this.clientVersion = clientVersion;
        this.expectedSignatureKey = expectedSignatureKey;
        this.usedSignatureKey = usedSignatureKey;
    }

    @Getter
    public String clientName() {
        return this.clientName;
    }

    @Getter
    public String clientVersion() {
        return this.clientVersion;
    }

    @Getter
    public String expectedSignatureKey() {
        return this.expectedSignatureKey;
    }

    @Getter
    public long usedSignatureKey() {
        return this.usedSignatureKey;
    }
}
