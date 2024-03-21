package com.github.cao.awa.lilium.config.template.bootstarp;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.annotations.auto.config.AutoConfigTemplate;
import com.github.cao.awa.lilium.config.bootstrap.Inner1Config;
import com.github.cao.awa.lilium.config.template.ConfigTemplate;

@Auto
@AutoConfigTemplate("./configs/bootstrap/inner-1.json")
public class Inner1ConfigTemplate extends ConfigTemplate<Inner1Config> {
}
