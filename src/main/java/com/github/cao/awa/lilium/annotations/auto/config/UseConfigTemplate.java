package com.github.cao.awa.lilium.annotations.auto.config;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.config.template.ConfigTemplate;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseConfigTemplate {
    Class<? extends ConfigTemplate<?>> value();
}
