
package com.hcq.elion.common.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.common.message.OkMessage;

/**
 */
public class OkMessageHandler extends BaseMessageHandler<OkMessage> {
    @Override
    public OkMessage decode(Packet packet, Connection connection) {
        return new OkMessage(packet, connection);
    }

    @Override
    public void handle(OkMessage message) {

    }
}
