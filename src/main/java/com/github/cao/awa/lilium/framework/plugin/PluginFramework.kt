package com.github.cao.awa.lilium.framework.plugin

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.lilium.annotation.auto.plugin.AutoPlugin
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework
import com.github.cao.awa.lilium.plugin.Plugin
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.Objects

class PluginFramework : ReflectionFramework() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("PluginFramework")
    }
    private val idToPlugin: MutableMap<String, Plugin> = ApricotCollectionFactor.hashMap()
    private val nameToPlugin: MutableMap<String, Plugin> = ApricotCollectionFactor.hashMap()

    override fun work() {
        reflection().getTypesAnnotatedWith(AutoPlugin::class.java)
            .filter {
                Plugin::class.java.isAssignableFrom(it)
            }.map(
                this::cast
            ).filter(
                Objects::nonNull
            ).forEach {
                build(it!!)
            }
    }

    private fun cast(clazz: Class<*>): Class<out Plugin>? {
        return EntrustEnvironment.cast(clazz)
    }

    private fun build(plugin: Class<out Plugin>) {
        val autoPlugin = plugin.getAnnotation(AutoPlugin::class.java)

        val pluginInstance = plugin.getConstructor().newInstance()
        this.idToPlugin[autoPlugin.id] = pluginInstance
        this.nameToPlugin[autoPlugin.name] = pluginInstance

        pluginInstance.onLoad()

        LOGGER.info("Loaded plugin '{}'({}) version '{}'", autoPlugin.name, autoPlugin.id, autoPlugin.version)
    }

    fun getPluginByName(name: String): Plugin? {
        return this.nameToPlugin[name]
    }

    fun getPluginById(id: String): Plugin? {
        return this.idToPlugin[id]
    }

    fun getPlugin(clazz: Class<out Plugin>): Plugin? {
        return this.idToPlugin[clazz.getAnnotation(AutoPlugin::class.java).id]
    }
}
