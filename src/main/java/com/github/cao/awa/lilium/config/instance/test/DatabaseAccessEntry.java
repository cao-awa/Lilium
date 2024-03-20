package com.github.cao.awa.lilium.config.instance.test;

import com.github.cao.awa.lilium.config.instance.ConfigEntry;

public class DatabaseAccessEntry<T> extends ConfigEntry<T> {
    private String key;

    public DatabaseAccessEntry() {

    }

    @Override
    public T get() {
        return super.get();
    }

    @Override
    public DatabaseAccessEntry<T> update(T value) {
        super.update(value);
        return this;
    }
}
