package com.github.cao.awa.lilium.network.encode.crypto;

public abstract class TransportLayerCrypto {
    public abstract byte[] encode(byte[] plains) throws Exception;

    public abstract byte[] decode(byte[] ciphertext) throws Exception;
}
