
package com.hcq.elion.core.router;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.event.RouterChangeEvent;
import com.hcq.elion.api.router.ClientLocation;
import com.hcq.elion.api.router.Router;
import com.hcq.elion.api.service.BaseService;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.common.router.RemoteRouter;
import com.hcq.elion.common.router.RemoteRouterManager;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.tools.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class RouterCenter extends BaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterCenter.class);

    private LocalRouterManager localRouterManager;
    private RemoteRouterManager remoteRouterManager;
    private UserEventConsumer userEventConsumer;
    private RouterChangeListener routerChangeListener;
    private ElionServer elionServer;

    public RouterCenter(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        localRouterManager = new LocalRouterManager();
        remoteRouterManager = new RemoteRouterManager();
        routerChangeListener = new RouterChangeListener(elionServer);
        userEventConsumer = new UserEventConsumer(remoteRouterManager);
        userEventConsumer.getUserManager().clearOnlineUserList();
        super.doStart(listener);
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        userEventConsumer.getUserManager().clearOnlineUserList();
        super.doStop(listener);
    }

    /**
     * 注册用户和链接
     *
     * @param userId
     * @param connection
     * @return
     */
    public boolean register(String userId, Connection connection) {
        ClientLocation location = ClientLocation
                .from(connection)
                .setHost(elionServer.getGatewayServerNode().getHost())
                .setPort(elionServer.getGatewayServerNode().getPort());

        LocalRouter localRouter = new LocalRouter(connection);
        RemoteRouter remoteRouter = new RemoteRouter(location);

        LocalRouter oldLocalRouter = null;
        RemoteRouter oldRemoteRouter = null;
        try {
            oldLocalRouter = localRouterManager.register(userId, localRouter);
            oldRemoteRouter = remoteRouterManager.register(userId, remoteRouter);
        } catch (Exception e) {
            LOGGER.error("register router ex, userId={}, connection={}", userId, connection, e);
        }

        if (oldLocalRouter != null) {
            EventBus.post(new RouterChangeEvent(userId, oldLocalRouter));
            LOGGER.info("register router success, find old local router={}, userId={}", oldLocalRouter, userId);
        }

        if (oldRemoteRouter != null && oldRemoteRouter.isOnline()) {
            EventBus.post(new RouterChangeEvent(userId, oldRemoteRouter));
            LOGGER.info("register router success, find old remote router={}, userId={}", oldRemoteRouter, userId);
        }
        return true;
    }

    public boolean unRegister(String userId, int clientType) {
        localRouterManager.unRegister(userId, clientType);
        remoteRouterManager.unRegister(userId, clientType);
        return true;
    }

    public Router<?> lookup(String userId, int clientType) {
        LocalRouter local = localRouterManager.lookup(userId, clientType);
        if (local != null) return local;
        RemoteRouter remote = remoteRouterManager.lookup(userId, clientType);
        return remote;
    }

    public LocalRouterManager getLocalRouterManager() {
        return localRouterManager;
    }

    public RemoteRouterManager getRemoteRouterManager() {
        return remoteRouterManager;
    }

    public RouterChangeListener getRouterChangeListener() {
        return routerChangeListener;
    }

    public UserEventConsumer getUserEventConsumer() {
        return userEventConsumer;
    }
}
