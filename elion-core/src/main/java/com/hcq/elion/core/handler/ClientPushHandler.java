

package com.hcq.elion.core.handler;


import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.message.MessageHandler;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.handler.PushHandlerFactory;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.AckMessage;
import com.hcq.elion.common.message.PushMessage;
import com.hcq.elion.tools.log.Logs;


@Spi(order = 1)
public final class ClientPushHandler extends BaseMessageHandler<PushMessage> implements PushHandlerFactory {

    @Override
    public PushMessage decode(Packet packet, Connection connection) {
        return new PushMessage(packet, connection);
    }

    @Override
    public void handle(PushMessage message) {
        Logs.PUSH.info("receive client push message={}", message);

        if (message.autoAck()) {
            AckMessage.from(message).sendRaw();
            Logs.PUSH.info("send ack for push message={}", message);
        }
        //biz code write here
    }

    @Override
    public MessageHandler get() {
        return this;
    }
}
