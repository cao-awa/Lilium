package com.github.cao.awa.lilium.network.packet.handler;

import com.github.cao.awa.lilium.exception.network.invalid.InvalidPacketException;
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.UnsolvedPacket;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.network.router.request.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public abstract class PacketHandler<H extends PacketHandler<H>> {
    private static final Logger LOGGER = LogManager.getLogger("PacketHandler");

    public Packet<H> handle(UnsolvedPacket<?> unsolved) {
        try {
            Packet<H> packet = EntrustEnvironment.cast(unsolved.packet());
            assert packet != null;

            return packet;
        } catch (Exception e) {
            throw new InvalidPacketException("Unsupported packet '" + unsolved + "'  in this handler: " + this);
        }
    }

    public abstract void inbound(Packet<H> packet, RequestRouter router);

    public void tryInbound(UnsolvedPacket<?> packet, RequestRouter router) {
        if (allowStates().contains(router.getStates())) {
            EntrustEnvironment.get(() -> {
                        inbound(
                                handle(packet),
                                router
                        );

                        return true;
                    },
                    false
            );
        }
    }

    public abstract Set<RequestState> allowStates();
}
