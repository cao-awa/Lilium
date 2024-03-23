package com.github.cao.awa.lilium.server;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.server.LiliumServerConfig;
import com.github.cao.awa.lilium.event.Event;

public class LiliumServer {
    @AutoConfig
    public final ConfigEntry<LiliumServerConfig> config = ConfigEntry.entry();

    public LiliumServer() {
        LiliumConfig.create(this);

        System.out.println(this.config.get().network.get().bindPort.get());
        System.out.println(this.config.get().network.get().metadata.get().configVersion.get());
    }

    public void fireEvent(Event event) {

    }
}
