package com.github.cao.awa.lilium.event.handler;

import com.github.cao.awa.lilium.event.Event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}
