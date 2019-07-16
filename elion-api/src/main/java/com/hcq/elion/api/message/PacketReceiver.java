
package com.hcq.elion.api.message;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;


public interface PacketReceiver {
    void onReceive(Packet packet, Connection connection);
}
