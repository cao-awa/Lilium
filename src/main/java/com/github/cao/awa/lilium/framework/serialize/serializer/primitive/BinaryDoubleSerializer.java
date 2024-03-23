package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@AutoSerializer({Double.class, double.class})
public class BinaryDoubleSerializer extends BinarySerializer<Double> {
    @Override
    public void serialize(Double d, ByteArrayOutputStream output) throws IOException {
        output.write(ByteBuffer.allocate(8)
                .putDouble(d)
                .array()
        );
    }

    @Override
    public Double deserialize(BytesReader reader) {
        return ByteBuffer.wrap(reader.read(8))
                .getDouble();
    }
}
