package com.github.cao.awa.lilium.config.template

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig
import com.github.cao.awa.lilium.config.LiliumConfig

@Auto
open class ConfigTemplate<T : LiliumConfig> {
    @Auto
    @AutoConfig
    private var config: T? = null
}
