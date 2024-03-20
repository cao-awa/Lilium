package com.github.cao.awa.lilium.config.template;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotations.config.AutoConfigTemplate;
import com.github.cao.awa.lilium.config.bootstrap.LiliumBootstrapConfig;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;

@Auto
@AutoConfigTemplate("./configs/config-metadata.json")
public class MetadataConfigTemplate extends ConfigTemplate<MetadataConfig> {
}
