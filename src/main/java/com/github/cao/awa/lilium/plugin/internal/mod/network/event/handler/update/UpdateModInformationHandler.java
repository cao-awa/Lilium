package com.github.cao.awa.lilium.plugin.internal.mod.network.event.handler.update;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.event.plugin.PluginRegister;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.plugin.internal.mod.MinecraftModInformationPlugin;
import com.github.cao.awa.lilium.plugin.internal.mod.network.packet.inbound.update.UpdateModInformationPacket;

@Auto
@PluginRegister(plugin = MinecraftModInformationPlugin.class)
public class UpdateModInformationHandler implements UpdateModInformationEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, UpdateModInformationPacket packet) {
        System.out.println(packet.modName());
    }
}
