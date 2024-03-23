package com.github.cao.awa.lilium.server;

import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.server.LiliumServerConfig;
import com.github.cao.awa.lilium.event.Event;
import com.github.cao.awa.lilium.network.io.server.KalmiaServerNetworkIo;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;

public class LiliumServer {
    @AutoConfig
    public final ConfigEntry<LiliumServerConfig> config = ConfigEntry.entry();
    private final KalmiaServerNetworkIo networkIo = new KalmiaServerNetworkIo(this);

    public LiliumServer() {
        LiliumConfig.create(this);
    }

    public void bootstrap() throws InterruptedException {
        this.networkIo.start();
    }

    public void fireEvent(Event event) {

    }

    public void logout(RequestRouter router) {

    }
}
