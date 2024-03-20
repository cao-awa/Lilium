package com.github.cao.awa.lilium.config.metadata;

import com.github.cao.awa.lilium.annotations.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.MetadataConfigTemplate;

@UseConfigTemplate(MetadataConfigTemplate.class)
public class MetadataConfig extends LiliumConfig {
    @AutoConfig("config-version")
    public ConfigEntry<Integer> configVersion;
}
