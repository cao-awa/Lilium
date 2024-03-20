package com.github.cao.awa.lilium.config.template.bootstarp;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotations.config.AutoConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.bootstrap.LiliumBootstrapConfig;
import com.github.cao.awa.lilium.config.template.ConfigTemplate;

@Auto
@AutoConfigTemplate("./configs/bootstrap/lilium-bootstrap.json")
public class LiliumBootstrapConfigTemplate extends ConfigTemplate<LiliumBootstrapConfig> {
}
