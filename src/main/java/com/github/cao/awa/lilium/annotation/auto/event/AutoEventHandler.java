package com.github.cao.awa.lilium.annotation.auto.event;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.lilium.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoEventHandler {
    Class<? extends Event> value();
}
