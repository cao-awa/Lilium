package com.github.cao.awa.lilium.client;

import com.github.cao.awa.lilium.annotation.auto.config.AutoConfig;
import com.github.cao.awa.lilium.config.client.LiliumClientConfig;
import com.github.cao.awa.lilium.config.instance.ConfigEntry;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;

import java.util.function.Consumer;

public class LiliumClient {
    @AutoConfig
    public final ConfigEntry<LiliumClientConfig> config = ConfigEntry.entry();
    private final Consumer<RequestRouter> activeCallback;
    private RequestRouter router;

    public LiliumClient(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        LiliumEnv.CONFIG_FRAMEWORK.createConfig(this);
    }

    public void router(RequestRouter router) {
        this.router = router;
    }

    public RequestRouter router() {
        return this.router;
    }

    public Consumer<RequestRouter> activeCallback() {
        return this.activeCallback;
    }
}
