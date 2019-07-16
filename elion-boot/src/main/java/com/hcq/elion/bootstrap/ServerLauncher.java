package com.hcq.elion.bootstrap;


import com.hcq.elion.api.common.ServerEventListener;
import com.hcq.elion.api.spi.core.ServerEventListenerFactory;
import com.hcq.elion.bootstrap.job.*;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.bootstrap.job.*;

import static com.hcq.elion.tools.config.CC.elion.net.tcpGateway;
import static com.hcq.elion.tools.config.CC.elion.net.udpGateway;
import static com.hcq.elion.tools.config.CC.elion.net.wsEnabled;

/**
 */
public final class ServerLauncher {

    private ElionServer elionServer;
    private BootChain chain;
    private ServerEventListener serverEventListener;

    public void init() {
        if (elionServer == null) {
            elionServer = new ElionServer();
        }

        if (chain == null) {
            chain = BootChain.chain();
        }

        if (serverEventListener == null) {
            serverEventListener = ServerEventListenerFactory.create();
        }

        serverEventListener.init(elionServer);

        chain.boot()
                .setNext(new CacheManagerBoot())//1.初始化缓存模块
                .setNext(new ServiceRegistryBoot())//2.启动服务注册与发现模块
                .setNext(new ServiceDiscoveryBoot())//2.启动服务注册与发现模块
                .setNext(new ServerBoot(elionServer.getConnectionServer(), elionServer.getConnServerNode()))//3.启动接入服务
                .setNext(() -> new ServerBoot(elionServer.getWebsocketServer(), elionServer.getWebsocketServerNode()), wsEnabled())//4.启动websocket接入服务
                .setNext(() -> new ServerBoot(elionServer.getUdpGatewayServer(), elionServer.getGatewayServerNode()), udpGateway())//5.启动udp网关服务
                .setNext(() -> new ServerBoot(elionServer.getGatewayServer(), elionServer.getGatewayServerNode()), tcpGateway())//6.启动tcp网关服务
                .setNext(new ServerBoot(elionServer.getAdminServer(), null))//7.启动控制台服务
                .setNext(new RouterCenterBoot(elionServer))//8.启动路由中心组件
                .setNext(new PushCenterBoot(elionServer))//9.启动推送中心组件
                .setNext(() -> new HttpProxyBoot(elionServer), CC.elion.http.proxy_enabled)//10.启动http代理服务，dns解析服务
                .setNext(new MonitorBoot(elionServer))//11.启动监控服务
                .end();
    }

    public void start() {
        chain.start();
    }

    public void stop() {
        chain.stop();
    }

    public void setLionServer(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    public void setChain(BootChain chain) {
        this.chain = chain;
    }

    public ElionServer getLionServer() {
        return elionServer;
    }

    public BootChain getChain() {
        return chain;
    }

    public ServerEventListener getServerEventListener() {
        return serverEventListener;
    }

    public void setServerEventListener(ServerEventListener serverEventListener) {
        this.serverEventListener = serverEventListener;
    }
}
