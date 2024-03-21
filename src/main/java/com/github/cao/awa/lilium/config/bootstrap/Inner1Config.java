package com.github.cao.awa.lilium.config.bootstrap;

import com.github.cao.awa.lilium.annotations.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.bootstarp.Inner1ConfigTemplate;

import java.util.List;
import java.util.Map;

@UseConfigTemplate(Inner1ConfigTemplate.class)
public class Inner1Config extends LiliumConfig {
    @AutoConfig("says")
    public ConfigEntry<Map<String, String>> says;
}
