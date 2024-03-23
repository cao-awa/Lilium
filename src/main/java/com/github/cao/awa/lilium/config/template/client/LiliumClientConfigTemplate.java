package com.github.cao.awa.lilium.config.template.client;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotation.auto.config.AutoConfigTemplate;
import com.github.cao.awa.lilium.config.client.LiliumClientConfig;
import com.github.cao.awa.lilium.config.server.LiliumServerConfig;
import com.github.cao.awa.lilium.config.template.ConfigTemplate;

@Auto
@AutoConfigTemplate("LiliumClientConfigTemplate")
public class LiliumClientConfigTemplate extends ConfigTemplate<LiliumClientConfig> {
}
