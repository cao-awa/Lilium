package com.github.cao.awa.lilium.plugin.internal.core.encryption.handler.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.plugin.PluginRegister;
import com.github.cao.awa.lilium.annotation.threading.ForceMainThread;
import com.github.cao.awa.lilium.env.LiliumPreSharedCipher;
import com.github.cao.awa.lilium.network.encode.crypto.asymmetric.any.AnyAsymmetricCrypto;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.LiliumEncryptionPlugin;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.hello.ClientHelloEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ClientHelloPacket;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.RequestingHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.security.PrivateKey;

@Auto
@Server
@ForceMainThread
@PluginRegister(plugin = LiliumEncryptionPlugin.class)
public class ClientHelloHandler implements ClientHelloEventHandler {
    @Override
    public void handle(RequestRouter router, ClientHelloPacket packet) {
        String expectedSignatureKey = packet.expectedSignatureKey();

        PrivateKey privateKey = LiliumPreSharedCipher.prikeyManager.get(expectedSignatureKey);
        if (privateKey == null) {
            expectedSignatureKey = router.useCipher();
            privateKey = LiliumPreSharedCipher.prikeyManager.get(expectedSignatureKey);
        }

        router.sendImmediately(new RequestingHandshakePacket(expectedSignatureKey));

        router.setCrypto(
                new AnyAsymmetricCrypto(
                        null,
                        privateKey
                )
        );
    }
}
