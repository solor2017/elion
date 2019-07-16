
package com.hcq.elion.client.gateway.connection;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.service.BaseService;
import com.hcq.elion.api.srd.ServiceListener;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.common.message.BaseMessage;
import com.hcq.elion.tools.config.CC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 */
public abstract class GatewayConnectionFactory extends BaseService implements ServiceListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static GatewayConnectionFactory create(ElionClient elionClient) {
        return CC.elion.net.udpGateway() ? new GatewayUDPConnectionFactory(elionClient) : new GatewayTCPConnectionFactory(elionClient);
    }

    abstract public Connection getConnection(String hostAndPort);

    abstract public <M extends BaseMessage> boolean send(String hostAndPort, Function<Connection, M> creator, Consumer<M> sender);

    abstract public <M extends BaseMessage> boolean broadcast(Function<Connection, M> creator, Consumer<M> sender);

}
