package com.github.cao.awa.lilium.plugin.internal.core.encryption.handler.handshake;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.lilium.annotation.auto.event.plugin.PluginRegister;
import com.github.cao.awa.lilium.annotation.threading.ForceMainThread;
import com.github.cao.awa.lilium.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.LiliumEncryptionPlugin;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.handshake.HandshakeAesEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.handshake.HandshakeAesPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ServerHelloPacket;

@Auto
@ForceMainThread
@PluginRegister(plugin = LiliumEncryptionPlugin.class)
public class HandshakeAesHandler implements HandshakeAesEventHandler {
    @Override
    public void handle(RequestRouter router, HandshakeAesPacket packet) {
        byte[] aesKey = packet.aesKey();

        router.setCrypto(new AesCrypto(aesKey));

        byte[] aesIv = BytesRandomIdentifier.create(16);

        router.sendImmediately(new ServerHelloPacket(aesIv));

        router.setIv(aesIv);
    }
}
