package com.github.cao.awa.lilium.env;

import com.github.cao.awa.lilium.framework.config.ConfigFramework;
import com.github.cao.awa.lilium.framework.serialize.BinarySerializeFramework;
import com.github.cao.awa.lilium.server.LiliumServer;

public class LiliumEnv {
    public static final ConfigFramework CONFIG_FRAMEWORK = new ConfigFramework();
    public static final BinarySerializeFramework BINARY_SERIALIZE_FRAMEWORK = new BinarySerializeFramework();

    public static void bootstrap() {
        CONFIG_FRAMEWORK.work();
        BINARY_SERIALIZE_FRAMEWORK.work();

        LiliumServer server = new LiliumServer();
    }
}
