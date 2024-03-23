package com.github.cao.awa.lilium.plugin

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.lilium.event.Event
import com.github.cao.awa.lilium.event.handler.EventHandler

@Auto
abstract class Plugin {
    private val eventHandlers: MutableMap<Class<out Event>, EventHandler<out Event>> = ApricotCollectionFactor.hashMap()

    abstract fun onLoad()
    abstract fun onUnload()
}
