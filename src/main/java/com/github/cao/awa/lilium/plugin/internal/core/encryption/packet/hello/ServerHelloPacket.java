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
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.hello.ServerHelloEvent;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@NetworkEventTarget(ServerHelloEvent.class)
@AutoSolvedPacket(crypto = false, stateless = true)
public class ServerHelloPacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.SERVER_HELLO;
    @AutoData
    private byte[] aesIv;

    @Auto
    @Client
    public ServerHelloPacket(BytesReader reader) {
        super(reader);
    }

    @Server
    public ServerHelloPacket(byte[] aesIv) {
        this.aesIv = aesIv;
    }

    @Getter
    public byte[] aesIv() {
        return this.aesIv;
    }
}
