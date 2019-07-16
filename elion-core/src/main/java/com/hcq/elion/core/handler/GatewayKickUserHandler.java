
package com.hcq.elion.core.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.gateway.GatewayKickUserMessage;
import com.hcq.elion.core.router.RouterCenter;


public final class GatewayKickUserHandler extends BaseMessageHandler<GatewayKickUserMessage> {

    private final RouterCenter routerCenter;

    public GatewayKickUserHandler(RouterCenter routerCenter) {
        this.routerCenter = routerCenter;
    }

    @Override
    public GatewayKickUserMessage decode(Packet packet, Connection connection) {
        return new GatewayKickUserMessage(packet, connection);
    }

    @Override
    public void handle(GatewayKickUserMessage message) {
        routerCenter.getRouterChangeListener().onReceiveKickRemoteMsg(message);
    }
}
