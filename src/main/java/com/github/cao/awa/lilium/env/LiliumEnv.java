package com.github.cao.awa.lilium.env;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.bootstrap.TestObj;
import com.github.cao.awa.lilium.framework.config.ConfigFramework;
import com.github.cao.awa.lilium.framework.serialize.BinarySerializeFramework;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LiliumEnv {
    public static final ConfigFramework CONFIG_FRAMEWORK = new ConfigFramework();
    public static final BinarySerializeFramework BINARY_SERIALIZE_FRAMEWORK = new BinarySerializeFramework();

    public static void bootstrap() {
        CONFIG_FRAMEWORK.work();
        BINARY_SERIALIZE_FRAMEWORK.work();

        TestObj test = new TestObj();
        CONFIG_FRAMEWORK.createConfig(test);
        test.test();

        test.bootstrapConfig.get().bindPort.update(1919810);

        TestObj test2 = new TestObj();
        CONFIG_FRAMEWORK.deepCopy(test, test2);

        test.bootstrapConfig.get().bindPort.update(114514);
        test.bootstrapConfig.get().metadata.get().configVersion.update(45678);

        test2.test();
        test.test();
    }
}
