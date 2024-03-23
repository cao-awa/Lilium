package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({Boolean.class, boolean.class})
public class BinaryBooleanSerializer extends BinarySerializer<Boolean> {
    @Override
    public void serialize(Boolean b, ByteArrayOutputStream output) throws IOException {
        output.write(b ? 1 : 0);
    }

    @Override
    public Boolean deserialize(BytesReader reader) {
        return reader.read() == 1;
    }
}
