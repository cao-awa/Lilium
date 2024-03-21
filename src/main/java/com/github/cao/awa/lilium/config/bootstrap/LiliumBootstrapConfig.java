package com.github.cao.awa.lilium.config.bootstrap;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;
import com.github.cao.awa.lilium.config.template.bootstarp.LiliumBootstrapConfigTemplate;
import org.jetbrains.annotations.NotNull;

@UseConfigTemplate(LiliumBootstrapConfigTemplate.class)
public class LiliumBootstrapConfig extends LiliumConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("metadata-2")
    public final ConfigEntry<MetadataConfig> metadata2 = ConfigEntry.entry();
    @AutoConfig("inner-1")
    public final ConfigEntry<Inner1Config> inner1 = ConfigEntry.entry();
    @AutoConfig("bind-port")
    public final ConfigEntry<Integer> bindPort = ConfigEntry.entry();
}
