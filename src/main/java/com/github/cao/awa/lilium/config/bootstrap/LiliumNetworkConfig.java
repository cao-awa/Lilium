package com.github.cao.awa.lilium.config.bootstrap;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;
import com.github.cao.awa.lilium.config.template.bootstarp.LiliumNetworkConfigTemplate;

@UseConfigTemplate(LiliumNetworkConfigTemplate.class)
public class LiliumNetworkConfig extends LiliumConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("bind-port")
    public final ConfigEntry<Integer> bindPort = ConfigEntry.entry();
    @AutoConfig("use-epoll")
    public final ConfigEntry<Boolean> useEpoll = ConfigEntry.entry();
}
