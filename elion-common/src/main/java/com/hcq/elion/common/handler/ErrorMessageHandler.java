
package com.hcq.elion.common.handler;

import com.hcq.elion.common.message.ErrorMessage;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;

/**
 */
public class ErrorMessageHandler extends BaseMessageHandler<ErrorMessage> {
    @Override
    public ErrorMessage decode(Packet packet, Connection connection) {
        return new ErrorMessage(packet, connection);
    }

    @Override
    public void handle(ErrorMessage message) {

    }
}
