package com.github.cao.awa.lilium.plugin.internal.core.user.packet.inbound.register;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.lilium.network.PacketIdentifier;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.stateless.StatelessHandler;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(crypto = false, stateless = true)
public class UserRegisterPacket extends Packet<StatelessHandler> {
    @Auto
    public static final byte[] ID = PacketIdentifier.USER_REGISTER;
    @AutoData
    @DoNotSet
    private String username;

    @Auto
    @Server
    public UserRegisterPacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public UserRegisterPacket(String username) {
        this.username = username;
    }

    @Getter
    public String username() {
        return this.username;
    }
}
