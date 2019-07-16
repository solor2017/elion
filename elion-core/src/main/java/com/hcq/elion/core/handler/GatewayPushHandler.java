
package com.hcq.elion.core.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.gateway.GatewayPushMessage;
import com.hcq.elion.core.push.PushCenter;


public final class GatewayPushHandler extends BaseMessageHandler<GatewayPushMessage> {

    private final PushCenter pushCenter;

    public GatewayPushHandler(PushCenter pushCenter) {
        this.pushCenter = pushCenter;
    }

    @Override
    public GatewayPushMessage decode(Packet packet, Connection connection) {
        return new GatewayPushMessage(packet, connection);
    }

    @Override
    public void handle(GatewayPushMessage message) {
        pushCenter.push(message);
    }
}
