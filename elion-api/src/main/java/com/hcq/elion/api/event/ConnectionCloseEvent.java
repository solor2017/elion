

package com.hcq.elion.api.event;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.connection.Connection;

public final class ConnectionCloseEvent implements Event {
    public final Connection connection;


    public ConnectionCloseEvent(Connection connection) {
        this.connection = connection;
    }
}
