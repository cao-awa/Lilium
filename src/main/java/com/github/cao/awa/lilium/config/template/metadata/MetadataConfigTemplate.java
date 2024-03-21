package com.github.cao.awa.lilium.config.template.metadata;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotations.auto.config.AutoConfigTemplate;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;
import com.github.cao.awa.lilium.config.template.ConfigTemplate;

@Auto
@AutoConfigTemplate("./configs/config-metadata.json")
public class MetadataConfigTemplate extends ConfigTemplate<MetadataConfig> {
}
