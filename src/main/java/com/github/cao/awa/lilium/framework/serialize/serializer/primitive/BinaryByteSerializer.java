package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({Byte.class, byte.class})
public class BinaryByteSerializer extends BinarySerializer<Byte> {
    @Override
    public void serialize(Byte b, ByteArrayOutputStream output) throws IOException {
        output.write(b);
    }

    @Override
    public Byte deserialize(BytesReader reader) {
        return reader.read();
    }
}
