

package com.hcq.elion.api.event;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.connection.Connection;


public final class ConnectionConnectEvent implements Event {
    public final Connection connection;

    public ConnectionConnectEvent(Connection connection) {
        this.connection = connection;
    }
}
