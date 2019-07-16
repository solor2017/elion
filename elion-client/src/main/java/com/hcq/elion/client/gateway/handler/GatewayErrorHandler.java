
package com.hcq.elion.client.gateway.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.client.push.PushRequest;
import com.hcq.elion.client.push.PushRequestBus;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.ErrorMessage;
import com.hcq.elion.tools.log.Logs;

import static com.hcq.elion.common.ErrorCode.OFFLINE;
import static com.hcq.elion.common.ErrorCode.PUSH_CLIENT_FAILURE;
import static com.hcq.elion.common.ErrorCode.ROUTER_CHANGE;

/**
 *
 */
public final class GatewayErrorHandler extends BaseMessageHandler<ErrorMessage> {

    private final PushRequestBus pushRequestBus;

    public GatewayErrorHandler(ElionClient elionClient) {
        this.pushRequestBus = elionClient.getPushRequestBus();
    }

    @Override
    public ErrorMessage decode(Packet packet, Connection connection) {
        return new ErrorMessage(packet, connection);
    }

    @Override
    public void handle(ErrorMessage message) {
        if (message.cmd == Command.GATEWAY_PUSH.cmd) {
            PushRequest request = pushRequestBus.getAndRemove(message.getSessionId());
            if (request == null) {
                Logs.PUSH.warn("receive a gateway response, but request has timeout. message={}", message);
                return;
            }

            Logs.PUSH.warn("receive an error gateway response, message={}", message);
            if (message.code == OFFLINE.errorCode) {//用户离线
                request.onOffline();
            } else if (message.code == PUSH_CLIENT_FAILURE.errorCode) {//下发到客户端失败
                request.onFailure();
            } else if (message.code == ROUTER_CHANGE.errorCode) {//用户路由信息更改
                request.onRedirect();
            }
        }
    }
}
