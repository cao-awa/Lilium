package com.github.cao.awa.lilium.annotation.auto.network.unsolve;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.apricot.annotations.auto.Auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AutoSolvedPacket {
    boolean crypto();
    boolean stateless() default false;
}
