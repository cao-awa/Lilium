package com.github.cao.awa.lilium.framework.network.event;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.lilium.annotation.auto.network.event.NetworkEventTarget;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.event.network.NetworkEvent;
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.PacketHandler;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.BiFunction;

public class NetworkEventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("NetworkEventFramework");
    private final Map<Class<? extends Packet<?>>, BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>>> events = ApricotCollectionFactor.hashMap();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                .stream()
                .filter(this::match)
                .map(this::cast)
                .forEach(this::build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(NetworkEventTarget.class) && Packet.class.isAssignableFrom(clazz);
    }

    public Class<? extends Packet<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends Packet<?>> clazz) {
        NetworkEventTarget target = clazz.getAnnotation(NetworkEventTarget.class);
        registerNetworkEvent(clazz,
                (router, packet) -> EntrustEnvironment.trys(() -> target.value()
                                .getConstructor(RequestRouter.class,
                                        clazz
                                )
                                .newInstance(router,
                                        packet
                                ),
                        ex -> {
                            ex.printStackTrace();
                            return;
                        }
                )
        );
    }

    public void registerNetworkEvent(Class<? extends Packet<?>> packet, BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>> creator) {
        this.events.put(packet,
                creator
        );
    }

    public void fireEvent(RequestRouter router, PacketHandler<?> handler, Packet<?> packet) {
        BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>> h = this.events.get(packet.getClass());
        if (h == null) {
            LOGGER.error("The packet '{}' has failed match the event handler, did annotation @NetworkEventTarget are missing in packet?",
                    packet.getClass()
                            .getName()
            );
        } else {
            LiliumEnv.EVENT_FRAMEWORK.fireEvent(h.apply(router,
                            packet
                    )
            );
        }
    }
}
