package com.github.cao.awa.lilium.config.bootstrap;

import com.github.cao.awa.lilium.annotations.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;
import com.github.cao.awa.lilium.config.template.bootstarp.LiliumBootstrapConfigTemplate;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

@UseConfigTemplate(LiliumBootstrapConfigTemplate.class)
public class LiliumBootstrapConfig extends LiliumConfig {
    @AutoConfig("metadata")
    public ConfigEntry<MetadataConfig> metadata;
    @AutoConfig("metadata-2")
    public ConfigEntry<MetadataConfig> metadata2;
    @AutoConfig("inner-1")
    public ConfigEntry<Inner1Config> inner1;
    @AutoConfig("bind-port")
    public ConfigEntry<Integer> bindPort;
}
