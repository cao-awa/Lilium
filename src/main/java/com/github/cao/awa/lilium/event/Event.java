package com.github.cao.awa.lilium.event;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.server.LiliumServer;

public abstract class Event {
    private LiliumServer server;

    public LiliumServer server() {
        return this.server;
    }

    @Auto
    private void fireEvent() {
        this.server.fireEvent(this);
    }
}
