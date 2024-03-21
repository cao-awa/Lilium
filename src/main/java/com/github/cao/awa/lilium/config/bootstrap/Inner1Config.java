package com.github.cao.awa.lilium.config.bootstrap;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.metadata.MetadataConfig;
import com.github.cao.awa.lilium.config.template.bootstarp.Inner1ConfigTemplate;

import java.util.List;

@UseConfigTemplate(Inner1ConfigTemplate.class)
public class Inner1Config extends LiliumConfig {
    @AutoConfig("list")
    public final ConfigEntry<List<MetadataConfig>> configs = ConfigEntry.entry();
    @AutoConfig("list2")
    public final ConfigEntry<List<Integer>> testList = ConfigEntry.entry();
}
