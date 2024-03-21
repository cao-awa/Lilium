package com.github.cao.awa.lilium.event.handler;

import com.github.cao.awa.lilium.event.Event;

public abstract class EventHandler<E extends Event> {
    public abstract void handle(E event);
}
