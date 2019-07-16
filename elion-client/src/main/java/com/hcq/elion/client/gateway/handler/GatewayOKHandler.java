
package com.hcq.elion.client.gateway.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.client.push.PushRequest;
import com.hcq.elion.client.push.PushRequestBus;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.OkMessage;
import com.hcq.elion.common.push.GatewayPushResult;
import com.hcq.elion.tools.log.Logs;

/**
 *
 */
public final class GatewayOKHandler extends BaseMessageHandler<OkMessage> {

    private PushRequestBus pushRequestBus;

    public GatewayOKHandler(ElionClient elionClient) {
        this.pushRequestBus = elionClient.getPushRequestBus();
    }

    @Override
    public OkMessage decode(Packet packet, Connection connection) {
        return new OkMessage(packet, connection);
    }

    @Override
    public void handle(OkMessage message) {
        if (message.cmd == Command.GATEWAY_PUSH.cmd) {
            PushRequest request = pushRequestBus.getAndRemove(message.getSessionId());
            if (request == null) {
                Logs.PUSH.warn("receive a gateway response, but request has timeout. message={}", message);
                return;
            }
            request.onSuccess(GatewayPushResult.fromJson(message.data));//推送成功
        }
    }
}
