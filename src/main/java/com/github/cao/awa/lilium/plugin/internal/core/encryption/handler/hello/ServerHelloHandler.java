package com.github.cao.awa.lilium.plugin.internal.core.encryption.handler.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.plugin.PluginRegister;
import com.github.cao.awa.lilium.annotation.threading.ForceMainThread;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.LiliumEncryptionPlugin;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.event.handler.hello.ServerHelloEventHandler;
import com.github.cao.awa.lilium.plugin.internal.core.encryption.packet.hello.ServerHelloPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.util.Arrays;

@Auto
@Client
@ForceMainThread
@PluginRegister(plugin = LiliumEncryptionPlugin.class)
public class ServerHelloHandler implements ServerHelloEventHandler {
    @Override
    public void handle(RequestRouter router, ServerHelloPacket packet) {
        router.setIv(packet.aesIv());
    }
}
