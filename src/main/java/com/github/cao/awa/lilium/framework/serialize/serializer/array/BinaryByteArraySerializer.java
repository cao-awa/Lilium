package com.github.cao.awa.lilium.framework.serialize.serializer.array;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({byte[].class, Byte[].class})
public class BinaryByteArraySerializer extends BinarySerializer<byte[]> {
    @Override
    public void serialize(byte[] bytes, ByteArrayOutputStream output) throws IOException {
        output.write(SkippedBase256.intToBuf(bytes.length));
        output.write(bytes);
    }

    @Override
    public byte[] deserialize(BytesReader reader) {
        return reader.read(SkippedBase256.readInt(reader));
    }
}
