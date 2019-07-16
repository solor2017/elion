
package com.hcq.elion.core.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.AckMessage;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.core.ack.AckTask;
import com.hcq.elion.core.ack.AckTaskQueue;
import com.hcq.elion.tools.log.Logs;


public final class AckHandler extends BaseMessageHandler<AckMessage> {

    private final AckTaskQueue ackTaskQueue;

    public AckHandler(ElionServer elionServer) {
        this.ackTaskQueue = elionServer.getPushCenter().getAckTaskQueue();
    }


    @Override
    public AckMessage decode(Packet packet, Connection connection) {
        return new AckMessage(packet, connection);
    }

    @Override
    public void handle(AckMessage message) {
        AckTask task = ackTaskQueue.getAndRemove(message.getSessionId());
        if (task == null) {//ack 超时了
            Logs.PUSH.info("receive client ack, but task timeout message={}", message);
            return;
        }

        task.onResponse();//成功收到客户的ACK响应
    }
}
