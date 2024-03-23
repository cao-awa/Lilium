package com.github.cao.awa.lilium.framework.serialize.serializer.string;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AutoSerializer({String.class})
public class BinaryStringSerializer extends BinarySerializer<String> {
    @Override
    public void serialize(String string, ByteArrayOutputStream output) throws IOException {
        output.write(SkippedBase256.intToBuf(string.length()));
        output.write(string.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String deserialize(BytesReader reader) {
        return new String(
                reader.read(SkippedBase256.readInt(reader)),
                StandardCharsets.UTF_8
        );
    }
}
