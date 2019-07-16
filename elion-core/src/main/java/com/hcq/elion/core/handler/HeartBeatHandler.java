
package com.hcq.elion.core.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.message.MessageHandler;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.tools.log.Logs;


public final class HeartBeatHandler implements MessageHandler {

    @Override
    public void handle(Packet packet, Connection connection) {
        connection.send(packet);//ping -> pong
        Logs.HB.info("ping -> pong, {}", connection);
    }
}
