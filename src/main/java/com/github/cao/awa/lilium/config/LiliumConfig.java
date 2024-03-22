package com.github.cao.awa.lilium.config;

import com.github.cao.awa.lilium.env.LiliumEnv;

public abstract class LiliumConfig {
    public static void create(Object target) {
        LiliumEnv.CONFIG_FRAMEWORK.createConfig(target);
    }
}
