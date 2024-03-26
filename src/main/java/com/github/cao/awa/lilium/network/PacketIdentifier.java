package com.github.cao.awa.lilium.network;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

public class PacketIdentifier {
    public static final byte[] TRY_DISCONNECT = longToBuf(0);

    // User manage
    public static final byte[] USER_REGISTER = longToBuf(1000000);

    // Minecraft mod information
    public static final byte[] UPDATE_MOD_INFORMATION = longToBuf(9000000);

    public static byte[] longToBuf(long value) {
        return SkippedBase256.longToBuf(value);
    }
}
