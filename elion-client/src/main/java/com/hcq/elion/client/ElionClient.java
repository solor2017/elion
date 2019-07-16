
package com.hcq.elion.client;

import com.hcq.elion.api.ElionContext;
import com.hcq.elion.api.spi.common.*;
import com.hcq.elion.api.srd.ServiceDiscovery;
import com.hcq.elion.api.srd.ServiceRegistry;
import com.hcq.elion.client.gateway.connection.GatewayConnectionFactory;
import com.hcq.elion.client.push.PushRequestBus;
import com.hcq.elion.common.router.CachedRemoteRouterManager;
import com.hcq.elion.monitor.service.MonitorService;
import com.hcq.elion.monitor.service.ThreadPoolManager;
import com.hcq.elion.tools.event.EventBus;

/**
 *
 */
public final class ElionClient implements ElionContext {

    private MonitorService monitorService;

    private PushRequestBus pushRequestBus;

    private CachedRemoteRouterManager cachedRemoteRouterManager;

    private GatewayConnectionFactory gatewayConnectionFactory;

    public ElionClient() {
        monitorService = new MonitorService();

        EventBus.create(monitorService.getThreadPoolManager().getEventBusExecutor());

        pushRequestBus = new PushRequestBus(this);

        cachedRemoteRouterManager = new CachedRemoteRouterManager();

        gatewayConnectionFactory = GatewayConnectionFactory.create(this);
    }

    public MonitorService getMonitorService() {
        return monitorService;
    }

    public ThreadPoolManager getThreadPoolManager() {
        return monitorService.getThreadPoolManager();
    }

    public PushRequestBus getPushRequestBus() {
        return pushRequestBus;
    }

    public CachedRemoteRouterManager getCachedRemoteRouterManager() {
        return cachedRemoteRouterManager;
    }

    public GatewayConnectionFactory getGatewayConnectionFactory() {
        return gatewayConnectionFactory;
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
