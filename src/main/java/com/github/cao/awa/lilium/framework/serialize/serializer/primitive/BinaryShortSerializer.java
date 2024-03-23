package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({Short.class, short.class})
public class BinaryShortSerializer extends BinarySerializer<Short> {
    @Override
    public void serialize(Short s, ByteArrayOutputStream output) throws IOException {
        output.write(SkippedBase256.tagToBuf(s));
    }

    @Override
    public Short deserialize(BytesReader reader) {
        return (short) Base256.readTag(reader);
    }
}
