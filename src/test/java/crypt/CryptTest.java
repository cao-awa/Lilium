package crypt;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import org.bouncycastle.pqc.jcajce.interfaces.FalconPrivateKey;
import org.bouncycastle.pqc.jcajce.interfaces.FalconPublicKey;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class CryptTest {
    public static void main(String[] args) {
        try {
            System.out.println("---Falcon");
            falcon();
            System.out.println("---ECC");
            ecc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void falcon() throws Exception {
        KeyPair keyPair = Crypto.falconKeypair(512);
        FalconPublicKey pubkey = (FalconPublicKey) keyPair.getPublic();
        FalconPrivateKey prikey = (FalconPrivateKey) keyPair.getPrivate();

        byte[] source = new byte[]{1,2,3,4,5,6};
        byte[] signed = Crypto.falconSign(source, prikey);
        System.out.println(Mathematics.radix(source, 36));
        System.out.println(Mathematics.radix(signed, 36));
        System.out.println(Crypto.falconVerify(source, signed, pubkey));
    }

    public static void ecc() throws Exception {
        KeyPair keyPair = Crypto.ecKeyPair(521);
        ECPublicKey pubkey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey prikey = (ECPrivateKey) keyPair.getPrivate();

        byte[] source = new byte[]{1,2,3,4,5,6};
        byte[] signed = Crypto.ecSign(source, prikey);
        System.out.println(Mathematics.radix(source, 36));
        System.out.println(Mathematics.radix(signed, 36));
        System.out.println(Crypto.ecVerify(source, signed, pubkey));
    }
}
