package com.github.cao.awa.lilium.security.key.manager;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.security.PublicKey;
import java.util.Map;

public class PubkeyManager {
    private final Map<String, PublicKey> publicKeys = ApricotCollectionFactor.hashMap();

    public void put(String field, PublicKey publicKey) {
        this.publicKeys.put(field, publicKey);
    }

    public PublicKey get(String field) {
        return this.publicKeys.get(field);
    }

    public PublicKey remove(String field) {
        return this.publicKeys.remove(field);
    }
}
