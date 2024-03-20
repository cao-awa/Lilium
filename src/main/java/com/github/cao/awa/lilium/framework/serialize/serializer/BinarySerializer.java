package com.github.cao.awa.lilium.framework.serialize.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public abstract class BinarySerializer {
    public abstract void serialize(Object object, Field field, ByteArrayOutputStream output) throws IOException;

    public abstract void deserialize(Object object, Field field, BytesReader reader) throws IllegalAccessException;

    public <T> T getAs(Object object, Field field, Class<T> clazz) {
        return EntrustEnvironment.trys(() -> clazz.cast(field.get(object)));
    }
}
