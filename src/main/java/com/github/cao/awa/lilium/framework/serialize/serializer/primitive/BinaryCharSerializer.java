package com.github.cao.awa.lilium.framework.serialize.serializer.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoSerializer({Character.class, char.class})
public class BinaryCharSerializer extends BinarySerializer<Character> {
    @Override
    public void serialize(Character c, ByteArrayOutputStream output) throws IOException {
        output.write(Base256.tagToBuf(c));
    }

    @Override
    public Character deserialize(BytesReader reader) {
        return (char) Base256.readTag(reader);
    }
}
