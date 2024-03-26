package com.github.cao.awa.lilium.plugin.internal.core.encryption.handler.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.lilium.annotation.auto.event.plugin.PluginRegister;
import com.github.cao.awa.lilium.annotation.threading.ForceMainThread;
import com.github.cao.awa.lilium.env.LiliumPreSharedCipher;
import com.github.cao.awa.lilium.network.encode.crypto.asymmetric.any.AnyAsymmetricCrypto;
import com.github.cao.awa.lilium.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.LiliumEncryptionPlugin;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.hello.RequestingHandshakeEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.handshake.HandshakeAesPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.RequestingHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@ForceMainThread
@PluginRegister(plugin = LiliumEncryptionPlugin.class)
public class RequestingHandshakeHandler implements RequestingHandshakeEventHandler {
    private final byte[] aesCipher = BytesRandomIdentifier.create(32);

    @Override
    public void handle(RequestRouter router, RequestingHandshakePacket packet) {
        router.setCrypto(new AnyAsymmetricCrypto(
                LiliumPreSharedCipher.pubkeyManager.get(packet.usedSignatureKey()),
                null
        ));
        router.sendImmediately(new HandshakeAesPacket(this.aesCipher));
        router.setCrypto(new AesCrypto(this.aesCipher));
    }
}
