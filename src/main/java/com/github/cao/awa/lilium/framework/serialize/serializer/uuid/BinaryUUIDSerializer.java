package com.github.cao.awa.lilium.framework.serialize.serializer.uuid;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.lilium.annotation.auto.serialize.AutoSerializer;
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@AutoSerializer({UUID.class})
public class BinaryUUIDSerializer extends BinarySerializer<UUID> {
    @Override
    public void serialize(UUID uuid, ByteArrayOutputStream output) throws IOException {
        output.write(BytesUtil.concat(
                Base256.longToBuf(uuid.getMostSignificantBits()),
                Base256.longToBuf(uuid.getLeastSignificantBits())
        ));
    }

    @Override
    public UUID deserialize(BytesReader reader) {
        return new UUID(Base256.readLong(reader),
                Base256.readLong(reader)
        );
    }
}
