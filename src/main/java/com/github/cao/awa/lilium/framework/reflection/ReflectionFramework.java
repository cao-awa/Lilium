package com.github.cao.awa.lilium.framework.reflection;

import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.lilium.framework.loader.JarSearchLoader;
import com.github.cao.awa.trtr.framework.accessor.method.MethodAccess;
import com.github.cao.awa.trtr.framework.exception.NoAutoAnnotationException;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public abstract class ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("ReflectionFramework");
    private static final Reflections REFLECTIONS = EntrustEnvironment.trys(() -> {
        File liliumJar = new File(URLDecoder.decode(
                ResourceLoader.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath(),
                StandardCharsets.UTF_8
        ));
        return new Reflections(new ConfigurationBuilder().addUrls(JarSearchLoader.load(new File("plugins")))
                .addUrls(ClasspathHelper.forPackage(""))
                .addUrls(liliumJar.toURI()
                        .toURL())
                .addScanners(Scanners.TypesAnnotated));
    });

    public Reflections reflection() {
        return REFLECTIONS;
    }

    public abstract void work();

    public boolean checkFields(String target, List<String> field) {
        if (field.size() > 0) {
            LOGGER.error("'{}' has missing required field(s): {}",
                    target,
                    field
            );
            return false;
        } else {
            LOGGER.debug("'{}' has passed checking required field(s)",
                    target
            );
            return true;
        }
    }

    public static <T> Constructor<T> ensureAccessible(Constructor<T> clazz) {
        if (clazz.canAccess(null)) {
            return clazz;
        }
        clazz.trySetAccessible();
        return clazz;
    }

    @NotNull
    public static Method ensureAccessible(Method clazz) {
        if (clazz.getAnnotation(Auto.class) != null) {
            return MethodAccess.ensureAccessible(clazz);
        }
        throw new NoAutoAnnotationException();
    }

    public static Field ensureAccessible(@NotNull Field field) {
        return ensureAccessible(field,
                null
        );
    }

    public static Field ensureAccessible(@NotNull Field field, @Nullable Object obj) {
        if (field.canAccess(Modifier.isStatic(field.getModifiers()) ? null : obj)) {
            return field;
        }
        field.trySetAccessible();
        return field;
    }

    public static Field fetchField(Class<?> clazz, @NotNull Object object, @NotNull String key) {
        if (clazz == null) {
            return null;
        }
        return EntrustEnvironment.trys(() -> ensureAccessible(clazz.getDeclaredField(key), object), e -> {
            return fetchField(clazz.getSuperclass(), object, key);
        });
    }

    public static Field fetchField(@NotNull Object object, @NotNull String key) {
        return EntrustEnvironment.trys(() -> ensureAccessible(object.getClass().getDeclaredField(key), object), e -> {
            return fetchField(object.getClass().getSuperclass(), object, key);
        });
    }

    public static Field fetchField(@NotNull Object object, @NotNull Field field) {
        return EntrustEnvironment.trys(() -> ensureAccessible(field, object));
    }

    public static Type[] getGenericType(@NotNull ParameterizedType parameterized) {
        return Arrays.stream(
                parameterized.getActualTypeArguments()
        ).toArray(Type[]::new);
    }

    public static Class<?> toClass(Type type) {
        return (Class<?>) type;
    }

    public static Type getArgType(Field field) {
        return getArgType(field.getGenericType());
    }

    public static Type getArgType(Type type) {
        if (type instanceof ParameterizedType parameterized) {
            return getGenericType(parameterized)[0];
        }
        return null;
    }

    public static <A extends Annotation> A getAnnotation(Object object, Class<A> annotation) {
        return object.getClass().getAnnotation(annotation);
    }

    public static Field[] getFields(Object object) {
        return object.getClass().getDeclaredFields();
    }
}
