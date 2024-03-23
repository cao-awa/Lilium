package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({Integer.class, int.class})
public class BinaryIntegerSerializer extends BinarySerializer<Integer> {
    @Override
    public void serialize(Integer i, ByteArrayOutputStream output) throws IOException {
        output.write(SkippedBase256.intToBuf(i));
    }

    @Override
    public Integer deserialize(BytesReader reader) {
        return SkippedBase256.readInt(reader);
    }
}
