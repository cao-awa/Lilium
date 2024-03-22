package com.github.cao.awa.lilium.config.server;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.bootstrap.LiliumNetworkConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.server.LiliumServerConfigTemplate;

@UseConfigTemplate(LiliumServerConfigTemplate.class)
public class LiliumServerConfig extends LiliumConfig {
    @AutoConfig("bootstrap")
    public final ConfigEntry<LiliumNetworkConfig> bootstrap = ConfigEntry.entry();
}
