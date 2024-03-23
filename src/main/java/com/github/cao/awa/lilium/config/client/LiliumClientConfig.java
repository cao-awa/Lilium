package com.github.cao.awa.lilium.config.client;

import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotation.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.client.LiliumClientConfigTemplate;

@UseConfigTemplate(LiliumClientConfigTemplate.class)
public class LiliumClientConfig extends LiliumConfig {
    @AutoConfig("server_host")
    public final ConfigEntry<String> serverHost = ConfigEntry.entry();
    @AutoConfig("server_port")
    public final ConfigEntry<Integer> serverPort = ConfigEntry.entry();
    @AutoConfig("use_epoll")
    public final ConfigEntry<Boolean> useEpoll = ConfigEntry.entry();
}
