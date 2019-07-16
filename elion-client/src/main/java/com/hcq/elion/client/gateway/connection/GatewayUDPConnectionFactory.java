
package com.hcq.elion.client.gateway.connection;

import com.google.common.collect.Maps;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.api.spi.common.ServiceDiscoveryFactory;
import com.hcq.elion.api.srd.ServiceDiscovery;
import com.hcq.elion.api.srd.ServiceNode;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.client.gateway.GatewayUDPConnector;
import com.hcq.elion.common.message.BaseMessage;
import com.hcq.elion.tools.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.hcq.elion.api.srd.ServiceNames.GATEWAY_SERVER;
import static com.hcq.elion.tools.config.CC.elion.net.gateway_server_multicast;
import static com.hcq.elion.tools.config.CC.elion.net.gateway_server_port;

/**
 */
public class GatewayUDPConnectionFactory extends GatewayConnectionFactory {

    private final Logger logger = LoggerFactory.getLogger(GatewayUDPConnectionFactory.class);

    private final Map<String, InetSocketAddress> ip_address = Maps.newConcurrentMap();

    private final InetSocketAddress multicastRecipient = new InetSocketAddress(gateway_server_multicast, gateway_server_port);

    private final GatewayUDPConnector gatewayUDPConnector;

    public GatewayUDPConnectionFactory(ElionClient elionClient) {
        gatewayUDPConnector = new GatewayUDPConnector(elionClient);
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        Utils.newThread("udp-client", () -> gatewayUDPConnector.start(listener)).start();
        ServiceDiscovery discovery = ServiceDiscoveryFactory.create();
        discovery.subscribe(GATEWAY_SERVER, this);
        discovery.lookup(GATEWAY_SERVER).forEach(this::addConnection);
    }

    @Override
    public void onServiceAdded(String path, ServiceNode node) {
        addConnection(node);
    }

    @Override
    public void onServiceUpdated(String path, ServiceNode node) {
        addConnection(node);
    }

    @Override
    public void onServiceRemoved(String path, ServiceNode node) {
        ip_address.remove(node.hostAndPort());
        logger.warn("Gateway Server zkNode={} was removed.", node);
    }

    private void addConnection(ServiceNode node) {
        ip_address.put(node.hostAndPort(), new InetSocketAddress(node.getHost(), node.getPort()));
    }

    @Override
    public void doStop(Listener listener) throws Throwable {
        ip_address.clear();
        gatewayUDPConnector.stop();
    }

    @Override
    public Connection getConnection(String hostAndPort) {
        return gatewayUDPConnector.getConnection();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <M extends BaseMessage> boolean send(String hostAndPort, Function<Connection, M> creator, Consumer<M> sender) {
        InetSocketAddress recipient = ip_address.get(hostAndPort);
        if (recipient == null) return false;// gateway server 找不到，直接返回推送失败

        M message = creator.apply(gatewayUDPConnector.getConnection());
        message.setRecipient(recipient);
        sender.accept(message);
        return true;
    }

    @Override
    public <M extends BaseMessage> boolean broadcast(Function<Connection, M> creator, Consumer<M> sender) {
        M message = creator.apply(gatewayUDPConnector.getConnection());
        message.setRecipient(multicastRecipient);
        sender.accept(message);
        return true;
    }

    public GatewayUDPConnector getGatewayUDPConnector() {
        return gatewayUDPConnector;
    }
}
