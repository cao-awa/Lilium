package com.github.cao.awa.lilium.config.metadata;

import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotation.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.metadata.MetadataConfigTemplate;

@UseConfigTemplate(MetadataConfigTemplate.class)
public class MetadataConfig extends LiliumConfig {
    @AutoConfig("config_version")
    public final ConfigEntry<Integer> configVersion = ConfigEntry.entry();
}
