package com.github.cao.awa.lilium.config.server;

import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotation.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.bootstrap.LiliumNetworkConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;
import com.github.cao.awa.lilium.config.template.server.LiliumServerConfigTemplate;

@UseConfigTemplate(LiliumServerConfigTemplate.class)
public class LiliumServerConfig extends LiliumConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("network")
    public final ConfigEntry<LiliumNetworkConfig> network = ConfigEntry.entry();
}
