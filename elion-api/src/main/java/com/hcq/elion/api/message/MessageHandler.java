
package com.hcq.elion.api.message;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;


public interface MessageHandler {
    void handle(Packet packet, Connection connection);
}
