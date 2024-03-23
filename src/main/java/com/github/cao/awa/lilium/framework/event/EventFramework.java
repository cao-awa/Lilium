package com.github.cao.awa.lilium.framework.event;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.thread.pool.ExecutorFactor;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.lilium.annotation.auto.event.AutoEventHandler;
import com.github.cao.awa.lilium.annotation.auto.event.plugin.PluginRegister;
import com.github.cao.awa.lilium.annotation.auto.plugin.AutoPlugin;
import com.github.cao.awa.lilium.annotation.util.AnnotationUtil;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.event.Event;
import com.github.cao.awa.lilium.event.handler.EventHandler;
import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.event.network.handler.NetworkEventHandler;
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework;
import com.github.cao.awa.lilium.plugin.Plugin;
import com.github.cao.awa.lilium.threading.ThreadingUtil;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class EventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("EventFramework");
    private final ExecutorService executor = ExecutorFactor.intensiveCpu();
    private final Map<Class<? extends Event>, List<EventHandler<?>>> handlers = ApricotCollectionFactor.hashMap();
    private final Map<EventHandler<?>, Class<? extends Plugin>> handlerBelongs = ApricotCollectionFactor.hashMap();
    private final Map<Class<? extends EventHandler<?>>, Class<? extends Event>> targetedEventHandlers = ApricotCollectionFactor.hashMap();
    private final List<Class<? extends EventHandler<?>>> registeredHandlers = ApricotCollectionFactor.arrayList();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(AutoEventHandler.class)
                .stream()
                .filter(this::match)
                .map(this::cast)
                .forEach(this::autoHandler);

        reflection().getTypesAnnotatedWith(Auto.class)
                .stream()
                .filter(this::match)
                .map(this::cast)
                .forEach(this::build);
    }

    public boolean match(Class<?> clazz) {
        return EventHandler.class.isAssignableFrom(clazz);
    }

    public Class<? extends EventHandler<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends EventHandler<?>> clazz) {
        if (Modifier.isInterface(clazz.getModifiers())) {
            return;
        }

        // Get plugin register annotation used to get plugin name to auto register.
        PluginRegister pluginAnnotation = clazz.getAnnotation(PluginRegister.class);

        if (pluginAnnotation == null) {
            return;
        }

        String pluginName = pluginAnnotation.plugin().getAnnotation(AutoPlugin.class).name();

        try {
            EventHandler<?> handler = clazz.getConstructor()
                    .newInstance();

            // Declare way to register.
            AutoEventHandler autoAnnotation = clazz.getAnnotation(AutoEventHandler.class);

            Consumer<Class<? extends Event>> adder = event -> this.handlers.compute(
                    event,
                    computeHandler(handler)
            );

            // Do potential coding problem tests.
            Set<AutoEventHandler> annotations = AnnotationUtil.getAnnotations(handler.getClass(),
                    AutoEventHandler.class
            );

            if (autoAnnotation == null) {
                // Do potential coding problem tests.
                if (annotations.size() > 1) {
                    // 当声明的目标超过1个时意味着一个处理器匹配了多个事件
                    // 但这是我们所不期待的，所以不要加载它
                    LOGGER.error(
                            "Class chains found the target over than 1 available declared, that wrongly, unable to register the '{}'",
                            handler.getClass()
                                    .getName()
                    );

                    return;
                }

                // 注册处理器
                for (Class<?> interfaceOf : clazz.getInterfaces()) {
                    adder.accept(target(EntrustEnvironment.cast(interfaceOf)));
                }

                LOGGER.info(
                        "Registered auto event handler '{}' via plugin '{}'",
                        handler.getClass()
                                .getName(),
                        pluginName
                );
            } else {
                //修正潜在的错误.
                if (annotations.size() == 2) {
                    annotations.remove(autoAnnotation);
                    LOGGER.warn(
                            "Targeted event handler '{}' declared a target '{}', but its superclass expected another target '{}', this may be a wrong, please check it",
                            handler.getClass()
                                    .getName(),
                            autoAnnotation.value()
                                    .getName(),
                            annotations.toArray(new AutoEventHandler[0])[0].value()
                    );
                } else if (annotations.size() > 2) {
                    // 当声明的目标超过1个时意味着一个处理器匹配了多个事件
                    // 但这是我们所不期待的，所以不要加载它
                    LOGGER.error(
                            "Class chains found the target over than 2 available declared, that wrongly, unable to register the '{}'",
                            handler.getClass()
                                    .getName()
                    );

                    return;
                }

                // 注册处理器
                adder.accept(autoAnnotation.value());

                LOGGER.info(
                        "Registered targeted event handler '{}' via plugin '{}'",
                        handler.getClass()
                                .getName(),
                        pluginName
                );
            }

            this.handlerBelongs.put(
                    handler,
                    pluginAnnotation.plugin()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerHandler(EventHandler<?> handler, Plugin plugin) {
        AutoEventHandler autoAnnotation = AnnotationUtil.getAnnotation(handler.getClass(),
                AutoEventHandler.class
        );

        if (autoAnnotation == null) {
            throw new IllegalStateException("");
        }

        this.handlers.compute(
                autoAnnotation.value(),
                computeHandler(handler)
        );

        this.handlerBelongs.put(
                handler,
                plugin.getClass()
        );

        LOGGER.info(
                "Registered manual event handler '{}' via plugin '{}'",
                handler.getClass()
                        .getName(),
                plugin.getClass().getAnnotation(AutoPlugin.class).name()
        );
    }

    public void optionalRegisterHandler(EventHandler<?> handler, Plugin plugin) {
        if (!this.registeredHandlers.contains(handler.getClass())) {
            registerHandler(handler,
                    plugin
            );
        }
    }

    public BiFunction<Class<? extends Event>, List<EventHandler<?>>, List<EventHandler<?>>> computeHandler(EventHandler<?> handler) {
        return (event, handlers) -> {
            if (handlers == null) {
                handlers = ApricotCollectionFactor.arrayList();
            }

            Class<? extends EventHandler<?>> handlerType = cast(handler.getClass());

            if (this.registeredHandlers.contains(handlerType)) {
                LOGGER.warn("Handler '{}' already registered, this action is repeated, may cause event be handled twice or more times",
                        handlerType
                );
            }
            handlers.add(handler);
            this.registeredHandlers.add(handlerType);
            return handlers;
        };
    }

    public Class<? extends EventHandler<?>> autoHandler(Class<? extends EventHandler<?>> handler) {
        if (Modifier.isInterface(handler.getModifiers())) {
            AutoEventHandler autoHandler = handler.getAnnotation(AutoEventHandler.class);

            if (autoHandler == null) {
                return handler;
            }

            this.targetedEventHandlers.put(handler,
                    autoHandler.value()
            );
        }

        return handler;
    }

    public Class<? extends Event> target(Class<? extends EventHandler<?>> handlerType) {
        return this.targetedEventHandlers.get(handlerType);
    }

    public void fireEvent(@NotNull Event event) {
        List<EventHandler<?>> handlers = this.handlers.get(event.getClass());

        if (missingHandler(
                handlers,
                event
        )) {
            return;
        }

        handlers.forEach(handler -> {
//            if (
                // If plugin are disabled, then do not let it handle events.
//                    plugin(handler).enabled()
//            ) {
                handleEvent(handler,
                        event
                );
//            }
        });
    }

    public void fireEvent(@NotNull NetworkEvent<?> event) {
        List<EventHandler<?>> handlers = this.handlers.get(event.getClass());

        if (missingHandler(
                handlers,
                event
        )) {
            return;
        }

//        this.executor.execute(() -> {
        for (EventHandler<?> handler : handlers) {
//            if (
//                // Network event can only handle by network event handler.
//                    handler instanceof NetworkEventHandler<?, ?> networkHandler &&
//                            // If plugin are disabled, then do not let it handle events.
//                            plugin(networkHandler).enabled()
//            ) {
                handleEvent(handler,
                        event
                );
//            }
        }
//        });
    }

    public void handleEvent(EventHandler<?> handler, Event event) {
        Runnable handleAction = () -> {
            handler.handle(EntrustEnvironment.cast(event));
        };

        if (ThreadingUtil.forceMainThread(handler.getClass())) {
            handleAction.run();
        } else {
            this.executor.execute(handleAction);
        }
    }

    public boolean missingHandler(List<EventHandler<?>> handlers, Event event) {
        if (handlers == null) {
            LOGGER.warn(
                    "No handler(s) can process the happening event '{}'",
                    event.getClass()
                            .getName()
            );
            return true;
        }
        return false;
    }

    public Plugin plugin(EventHandler<?> handler) {
        Class<? extends Plugin> id = this.handlerBelongs.get(handler);
        return LiliumEnv.PLUGIN_FRAMEWORK.getPlugin(
                id != null ? id :
                        handler.getClass()
                                .getAnnotation(PluginRegister.class)
                                .plugin()
        );
    }
}
