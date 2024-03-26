package com.github.cao.awa.lilium.network;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

public class PacketIdentifier {
    public static final byte[] TRY_DISCONNECT = longToBuf(0);

    // Lilium encryption
    public static final byte[] CLIENT_HELLO = longToBuf(1000000);
    public static final byte[] REQUESTING_HANDSHAKE = longToBuf(1000001);
    public static final byte[] HANDSHAKE_AES = longToBuf(1000002);
    public static final byte[] SERVER_HELLO = longToBuf(1000003);


    // User manage
    public static final byte[] USER_REGISTER = longToBuf(2000000);

    // Minecraft mod information
    public static final byte[] UPDATE_MOD_INFORMATION = longToBuf(9000000);

    public static byte[] longToBuf(long value) {
        return SkippedBase256.longToBuf(value);
    }
}
