package com.github.cao.awa.lilium.network;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

public class PacketIdentifier {
    public static final byte[] TRY_DISCONNECT = longToBuf(0);

    public static byte[] longToBuf(long value) {
        return SkippedBase256.longToBuf(value);
    }
}
