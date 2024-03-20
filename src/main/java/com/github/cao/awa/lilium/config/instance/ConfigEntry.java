package com.github.cao.awa.lilium.config.instance;

public class ConfigEntry<T> {
    private String key;
    private T value;

    public ConfigEntry(Class<T> specifyType) {

    }

    public ConfigEntry() {

    }

    public String key() {
        return this.key;
    }

    public T get() {
        return this.value;
    }

    public ConfigEntry<T> update(T value) {
        this.value = value;
        return this;
    }
}
