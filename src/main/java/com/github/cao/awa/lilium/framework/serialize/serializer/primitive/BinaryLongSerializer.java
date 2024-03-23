package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({Long.class, long.class})
public class BinaryLongSerializer extends BinarySerializer<Long> {
    @Override
    public void serialize(Long l, ByteArrayOutputStream output) throws IOException {
        output.write(SkippedBase256.longToBuf(l));
    }

    @Override
    public Long deserialize(BytesReader reader) {
        return SkippedBase256.readLong(reader);
    }
}
