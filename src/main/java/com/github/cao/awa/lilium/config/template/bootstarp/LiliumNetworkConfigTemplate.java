package com.github.cao.awa.lilium.config.template.bootstarp;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotations.auto.config.AutoConfigTemplate;
import com.github.cao.awa.lilium.config.bootstrap.LiliumNetworkConfig;
import com.github.cao.awa.lilium.config.template.ConfigTemplate;

@Auto
@AutoConfigTemplate("./configs/bootstrap/network/lilium-bootstrap-network.json")
public class LiliumNetworkConfigTemplate extends ConfigTemplate<LiliumNetworkConfig> {
}
