package com.github.cao.awa.lilium.env;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.bootstrap.TestObj;
import com.github.cao.awa.lilium.framework.config.ConfigFramework;
import com.github.cao.awa.lilium.framework.serialize.BinarySerializeFramework;

import java.util.Arrays;

public class LiliumEnv {
    public static final ConfigFramework CONFIG_FRAMEWORK = new ConfigFramework();
    public static final BinarySerializeFramework BINARY_SERIALIZE_FRAMEWORK = new BinarySerializeFramework();

    public static void bootstrap() {
        CONFIG_FRAMEWORK.work();
        BINARY_SERIALIZE_FRAMEWORK.work();

        TestObj test = new TestObj();
        CONFIG_FRAMEWORK.createConfig(test);
        test.test();

        TestObj test2 = new TestObj();
        CONFIG_FRAMEWORK.deepCopy(test, test2);
        test2.test();
    }
}
