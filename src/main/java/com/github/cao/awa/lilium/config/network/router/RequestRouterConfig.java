package com.github.cao.awa.lilium.config.network.router;

import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.annotation.auto.config.UseConfigTemplate;
import com.github.cao.awa.lilium.config.LiliumConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.config.template.network.router.RequestRouterConfigTemplate;

@UseConfigTemplate(RequestRouterConfigTemplate.class)
public class RequestRouterConfig extends LiliumConfig {
    @AutoConfig("compress_threshold")
    public final ConfigEntry<Integer> compressThreshold = ConfigEntry.entry();
}
