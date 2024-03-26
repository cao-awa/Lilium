package com.github.cao.awa.lilium.security.key.manager;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.security.PrivateKey;
import java.util.Map;

public class PrikeyManager {
    private final Map<String, PrivateKey> privateKeys = ApricotCollectionFactor.hashMap();

    public void put(String field, PrivateKey PrivateKey) {
        this.privateKeys.put(field, PrivateKey);
    }

    public PrivateKey get(String field) {
        return this.privateKeys.get(field);
    }

    public PrivateKey remove(String field) {
        return this.privateKeys.remove(field);
    }
}
