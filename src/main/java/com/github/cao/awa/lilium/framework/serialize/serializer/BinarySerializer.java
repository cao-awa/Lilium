package com.github.cao.awa.lilium.framework.serialize.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public abstract class BinarySerializer<T> {
    public abstract void serialize(T object, ByteArrayOutputStream output) throws IOException;

    public abstract T deserialize(BytesReader reader) throws IllegalAccessException;

    public <X> X getAs(Object object, Class<X> clazz) {
        return EntrustEnvironment.trys(() -> clazz.cast(object));
    }
}
