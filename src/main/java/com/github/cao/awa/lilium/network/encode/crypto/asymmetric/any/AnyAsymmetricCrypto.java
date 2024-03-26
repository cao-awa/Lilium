package com.github.cao.awa.lilium.network.encode.crypto.asymmetric.any;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.lilium.network.encode.crypto.asymmetric.AsymmetricCrypto;
import org.jetbrains.annotations.Nullable;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class AnyAsymmetricCrypto extends AsymmetricCrypto {
    public AnyAsymmetricCrypto(@Nullable PublicKey pubkey, @Nullable PrivateKey prikey) {
        super(pubkey,
                prikey
        );
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        if (pubkey() == null) {
            return plains;
        }
        return Crypto.asymmetricEncrypt(plains,
                pubkey()
        );
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        if (prikey() == null) {
            return ciphertext;
        }
        return Crypto.asymmetricDecrypt(ciphertext,
                prikey()
        );
    }
}
