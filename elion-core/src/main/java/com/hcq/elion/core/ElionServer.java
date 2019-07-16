
package com.hcq.elion.core;

import com.hcq.elion.api.ElionContext;
import com.hcq.elion.api.spi.common.*;
import com.hcq.elion.api.srd.ServiceDiscovery;
import com.hcq.elion.api.srd.ServiceNode;
import com.hcq.elion.api.srd.ServiceRegistry;
import com.hcq.elion.common.ServerNodes;
import com.hcq.elion.core.push.PushCenter;
import com.hcq.elion.core.router.RouterCenter;
import com.hcq.elion.core.server.*;
import com.hcq.elion.core.session.ReusableSessionManager;
import com.hcq.elion.monitor.service.MonitorService;
import com.hcq.elion.network.netty.http.HttpClient;
import com.hcq.elion.network.netty.http.NettyHttpClient;
import com.hcq.elion.tools.event.EventBus;

import static com.hcq.elion.tools.config.CC.elion.net.tcpGateway;

public final class ElionServer implements ElionContext {

    private ServiceNode connServerNode;
    private ServiceNode gatewayServerNode;
    private ServiceNode websocketServerNode;

    private ConnectionServer connectionServer;
    private WebsocketServer websocketServer;
    private GatewayServer gatewayServer;
    private AdminServer adminServer;
    private GatewayUDPConnector udpGatewayServer;

    private HttpClient httpClient;

    private PushCenter pushCenter;

    private ReusableSessionManager reusableSessionManager;

    private RouterCenter routerCenter;

    private MonitorService monitorService;


    public ElionServer() {
        connServerNode = ServerNodes.cs();
        gatewayServerNode = ServerNodes.gs();
        websocketServerNode = ServerNodes.ws();

        monitorService = new MonitorService();
        EventBus.create(monitorService.getThreadPoolManager().getEventBusExecutor());

        reusableSessionManager = new ReusableSessionManager();

        pushCenter = new PushCenter(this);

        routerCenter = new RouterCenter(this);

        connectionServer = new ConnectionServer(this);

        websocketServer = new WebsocketServer(this);

        adminServer = new AdminServer(this);

        if (tcpGateway()) {
            gatewayServer = new GatewayServer(this);
        } else {
            udpGatewayServer = new GatewayUDPConnector(this);
        }
    }

    public boolean isTargetMachine(String host, int port) {
        return port == gatewayServerNode.getPort() && gatewayServerNode.getHost().equals(host);
    }

    public ServiceNode getConnServerNode() {
        return connServerNode;
    }

    public ServiceNode getGatewayServerNode() {
        return gatewayServerNode;
    }

    public ServiceNode getWebsocketServerNode() {
        return websocketServerNode;
    }

    public ConnectionServer getConnectionServer() {
        return connectionServer;
    }

    public GatewayServer getGatewayServer() {
        return gatewayServer;
    }

    public AdminServer getAdminServer() {
        return adminServer;
    }

    public GatewayUDPConnector getUdpGatewayServer() {
        return udpGatewayServer;
    }

    public WebsocketServer getWebsocketServer() {
        return websocketServer;
    }

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    httpClient = new NettyHttpClient();
                }
            }
        }
        return httpClient;
    }

    public PushCenter getPushCenter() {
        return pushCenter;
    }

    public ReusableSessionManager getReusableSessionManager() {
        return reusableSessionManager;
    }

    public RouterCenter getRouterCenter() {
        return routerCenter;
    }

    @Override
    public MonitorService getMonitor() {
        return monitorService;
    }

    @Override
    public ServiceDiscovery getDiscovery() {
        return ServiceDiscoveryFactory.create();
    }

    @Override
    public ServiceRegistry getRegistry() {
        return ServiceRegistryFactory.create();
    }

    @Override
    public CacheManager getCacheManager() {
        return CacheManagerFactory.create();
    }

    @Override
    public MQClient getMQClient() {
        return MQClientFactory.create();
    }
}
