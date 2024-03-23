package com.github.cao.awa.lilium.config.constant;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.constants.LiliumConstantsConfigTemplate;

@UseConfigTemplate(LiliumConstantsConfigTemplate.class)
public class LiliumConstantsConfig extends LiliumConfig {
    @AutoConfig("true")
    public final ConfigEntry<Boolean> trueValue = ConfigEntry.entry();
    @AutoConfig("false")
    public final ConfigEntry<Boolean> falseValue = ConfigEntry.entry();
    @AutoConfig("k")
    public final ConfigEntry<Integer> k = ConfigEntry.entry();
    @AutoConfig("k16")
    public final ConfigEntry<Integer> k16 = ConfigEntry.entry();
    @AutoConfig("int_max")
    public final ConfigEntry<Integer> intMax = ConfigEntry.entry();
    @AutoConfig("long_max")
    public final ConfigEntry<Long> longMax = ConfigEntry.entry();
}
