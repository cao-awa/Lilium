package com.github.cao.awa.lilium.bootstrap;

import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig;
import com.github.cao.awa.lilium.config.bootstrap.LiliumBootstrapConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.instance.test.DatabaseAccessEntry;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

public class TestObj {
    @AutoConfig
    public DatabaseAccessEntry<String> strConfig = new DatabaseAccessEntry<>();
    @AutoConfig("int-config")
    public ConfigEntry<Integer> intConfig = EntrustEnvironment.cast(new ConfigEntry<>().update(12345));
    @AutoConfig
    public ConfigEntry<LiliumBootstrapConfig> bootstrapConfig;

    public void test() {
        System.out.println(this.strConfig.get());
        System.out.println(this.intConfig.get());
        System.out.println(this.bootstrapConfig.get().bindPort.get());
        System.out.println(this.bootstrapConfig.get().metadata.get().configVersion.get());
        System.out.println(this.bootstrapConfig.get().metadata2.get().configVersion.get());
        System.out.println(this.bootstrapConfig.get().inner1.get().configs.get());
    }
}
