package com.github.cao.awa.lilium.env;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.env.security.key.PreSharedCipherEncode;
import com.github.cao.awa.lilium.security.key.manager.PrikeyManager;
import com.github.cao.awa.lilium.security.key.manager.PubkeyManager;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class LiliumPreSharedCipher {
    private static final Logger LOGGER = LogManager.getLogger("LiliumPreSharedCipher");
    public static final PubkeyManager pubkeyManager = new PubkeyManager();
    public static final PrikeyManager prikeyManager = new PrikeyManager();

    public static void setupCiphers() {
        LOGGER.info("Preparing ciphers");

        File ciphersDir = new File(LiliumEnv.constants.ciphersDirectory.get());
        File[] cipherDirs = ciphersDir.listFiles();

        if (cipherDirs != null) {
            for (File cipherDir : cipherDirs) {
                setupCipher(cipherDir.getAbsolutePath());
            }
        }
    }

    public static void setupCipher(String cipherDir) {
        File metaFile = new File(cipherDir + "/cipher.json");
        File secretPublicFile = new File(cipherDir + "/SECRET_PUBLIC");
        File secretPrivateFile = new File(cipherDir + "/SECRET_PRIVATE");

        if (! metaFile.isFile()) {
            LOGGER.warn("The cipher in '{}' is missing metadata file, will not be load",
                        metaFile.getAbsolutePath()
            );
        }

        JSONObject metadata = EntrustEnvironment.trys(() -> JSONObject.parse(IOUtil.read(new FileReader(metaFile))));

        assert metadata != null;
        String cipherName = metadata.getString("cipher-name");

        LOGGER.info("Loading cipher '{}'",
                    cipherName
        );

        setupPublicKey(secretPublicFile,
                       cipherName
        );

        setupPrivateKey(secretPrivateFile,
                        cipherName
        );
    }

    public static void setupPublicKey(File file, String fieldName) {
        if (file.isFile()) {
            JSONObject secretPublic = EntrustEnvironment.trys(() -> JSONObject.parse(IOUtil.read(new FileReader(file))));

            if (secretPublic != null) {
                ECPublicKey publicKey = PreSharedCipherEncode.decodeEcPublic(secretPublic);

                pubkeyManager.put(fieldName,
                                  publicKey
                );
            }
        }
    }

    public static void setupPrivateKey(File file, String fieldName) {
        if (file.isFile()) {
            JSONObject secretPrivate = EntrustEnvironment.trys(() -> JSONObject.parse(IOUtil.read(new FileReader(file))));

            if (secretPrivate != null) {
                ECPrivateKey publicKey = PreSharedCipherEncode.decodeEcPrivate(secretPrivate);

                prikeyManager.put(fieldName,
                                  publicKey
                );
            }
        }
    }
}
