package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotations.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

@AutoSerializer({Integer.class, int.class})
public class BinaryIntegerSerializer extends BinarySerializer {
    @Override
    public void serialize(Object object, Field field, ByteArrayOutputStream output) throws IOException {
        output.write(SkippedBase256.intToBuf(getAs(object, field, Integer.class)));
    }

    @Override
    public void deserialize(Object object, Field field, BytesReader reader) throws IllegalAccessException {
        field.set(object, SkippedBase256.readInt(reader));
    }
}
