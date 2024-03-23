package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

@AutoSerializer({Float.class, float.class})
public class BinaryFloatSerializer extends BinarySerializer<Float> {
    @Override
    public void serialize(Float f, ByteArrayOutputStream output) throws IOException {
        output.write(ByteBuffer.allocate(4)
                .putFloat(f)
                .array()
        );
    }

    @Override
    public Float deserialize(BytesReader reader) {
        return ByteBuffer.wrap(reader.read(4))
                .getFloat();
    }
}
