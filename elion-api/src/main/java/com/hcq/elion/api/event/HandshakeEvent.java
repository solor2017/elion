

package com.hcq.elion.api.event;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.connection.Connection;


public final class HandshakeEvent implements Event {
    public final Connection connection;
    public final int heartbeat;

    public HandshakeEvent(Connection connection, int heartbeat) {
        this.connection = connection;
        this.heartbeat = heartbeat;
    }
}
