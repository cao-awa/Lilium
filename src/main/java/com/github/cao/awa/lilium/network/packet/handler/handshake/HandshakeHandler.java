package com.github.cao.awa.lilium.network.packet.handler.handshake;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.lilium.network.packet.Packet;
import com.github.cao.awa.lilium.network.packet.handler.PacketHandler;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.lilium.network.router.request.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class HandshakeHandler extends PacketHandler<HandshakeHandler> {
    private static final Set<RequestState> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.hashSet(),
            set -> {
                set.add(RequestState.HELLO);
            }
    );

    public HandshakeHandler() {

    }

    @Override
    public void inbound(Packet<HandshakeHandler> packet, RequestRouter router) {
        packet.inbound(router,
                this
        );
    }

    @Override
    public Set<RequestState> allowStates() {
        return ALLOW_STATUS;
    }
}
