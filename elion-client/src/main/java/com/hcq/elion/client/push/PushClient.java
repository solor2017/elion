
package com.hcq.elion.client.push;

import com.hcq.elion.api.ElionContext;
import com.hcq.elion.api.push.PushContext;
import com.hcq.elion.api.push.PushException;
import com.hcq.elion.api.push.PushResult;
import com.hcq.elion.api.push.PushSender;
import com.hcq.elion.api.service.BaseService;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.api.spi.common.CacheManagerFactory;
import com.hcq.elion.api.spi.common.ServiceDiscoveryFactory;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.client.gateway.connection.GatewayConnectionFactory;
import com.hcq.elion.common.router.CachedRemoteRouterManager;
import com.hcq.elion.common.router.RemoteRouter;

import java.util.Set;
import java.util.concurrent.FutureTask;

public final class PushClient extends BaseService implements PushSender {

    private ElionClient elionClient;

    private PushRequestBus pushRequestBus;

    private CachedRemoteRouterManager cachedRemoteRouterManager;

    private GatewayConnectionFactory gatewayConnectionFactory;

    private FutureTask<PushResult> send0(PushContext ctx) {
        if (ctx.isBroadcast()) {
            return PushRequest.build(elionClient, ctx).broadcast();
        } else {
            Set<RemoteRouter> remoteRouters = cachedRemoteRouterManager.lookupAll(ctx.getUserId());
            if (remoteRouters == null || remoteRouters.isEmpty()) {
                return PushRequest.build(elionClient, ctx).onOffline();
            }
            FutureTask<PushResult> task = null;
            for (RemoteRouter remoteRouter : remoteRouters) {
                task = PushRequest.build(elionClient, ctx).send(remoteRouter);
            }
            return task;
        }
    }

    @Override
    public FutureTask<PushResult> send(PushContext ctx) {
        if (ctx.isBroadcast()) {
            return send0(ctx.setUserId(null));
        } else if (ctx.getUserId() != null) {
            return send0(ctx);
        } else if (ctx.getUserIds() != null) {
            FutureTask<PushResult> task = null;
            for (String userId : ctx.getUserIds()) {
                task = send0(ctx.setUserId(userId));
            }
            return task;
        } else {
            throw new PushException("param error.");
        }
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        if (elionClient == null) {
            elionClient = new ElionClient();
        }

        pushRequestBus = elionClient.getPushRequestBus();
        cachedRemoteRouterManager = elionClient.getCachedRemoteRouterManager();
        gatewayConnectionFactory = elionClient.getGatewayConnectionFactory();

        ServiceDiscoveryFactory.create().syncStart();
        CacheManagerFactory.create().init();
        pushRequestBus.syncStart();
        gatewayConnectionFactory.start(listener);
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        ServiceDiscoveryFactory.create().syncStop();
        CacheManagerFactory.create().destroy();
        pushRequestBus.syncStop();
        gatewayConnectionFactory.stop(listener);
    }

    @Override
    public boolean isRunning() {
        return started.get();
    }

    @Override
    public void setLionContext(ElionContext context) {
        this.elionClient = ((ElionClient) context);
    }
}
