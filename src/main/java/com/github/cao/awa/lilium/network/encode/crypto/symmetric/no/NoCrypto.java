package com.github.cao.awa.lilium.network.encode.crypto.symmetric.no;

import com.github.cao.awa.lilium.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class NoCrypto extends SymmetricCrypto {
    public NoCrypto() {
        super(BytesUtil.EMPTY);
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        return plains;
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        return ciphertext;
    }
}
