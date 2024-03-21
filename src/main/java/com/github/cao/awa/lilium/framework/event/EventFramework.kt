package com.github.cao.awa.lilium.framework.event

import com.github.cao.awa.lilium.event.Event
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework
import com.github.cao.awa.lilium.server.LiliumServer

class EventFramework : ReflectionFramework() {
    override fun work() {
    }

    fun fireEvent(server: LiliumServer, event: Event) {
        // 设置
        fetchField(event, "server")[event] = server


    }
}
