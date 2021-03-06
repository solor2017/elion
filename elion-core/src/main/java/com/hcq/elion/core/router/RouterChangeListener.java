
package com.hcq.elion.core.router;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.hcq.elion.api.Constants;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.connection.SessionContext;
import com.hcq.elion.api.event.RouterChangeEvent;
import com.hcq.elion.api.router.ClientLocation;
import com.hcq.elion.api.router.Router;
import com.hcq.elion.api.spi.common.MQClient;
import com.hcq.elion.api.spi.common.MQClientFactory;
import com.hcq.elion.api.spi.common.MQMessageReceiver;
import com.hcq.elion.common.message.KickUserMessage;
import com.hcq.elion.common.message.gateway.GatewayKickUserMessage;
import com.hcq.elion.common.router.KickRemoteMsg;
import com.hcq.elion.common.router.MQKickRemoteMsg;
import com.hcq.elion.common.router.RemoteRouter;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.tools.Jsons;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.config.ConfigTools;
import com.hcq.elion.tools.event.EventConsumer;
import com.hcq.elion.tools.log.Logs;

import java.net.InetSocketAddress;

import static com.hcq.elion.api.Constants.KICK_CHANNEL_PREFIX;



public final class RouterChangeListener extends EventConsumer implements MQMessageReceiver {
    private final boolean udpGateway = CC.elion.net.udpGateway();
    private String kick_channel;
    private MQClient mqClient;
    private ElionServer elionServer;

    public RouterChangeListener(ElionServer elionServer) {
        this.elionServer = elionServer;
        this.kick_channel = KICK_CHANNEL_PREFIX + elionServer.getGatewayServerNode().hostAndPort();
        if (!udpGateway) {
            mqClient = MQClientFactory.create();
            mqClient.init(elionServer);
            mqClient.subscribe(getKickChannel(), this);
        }
    }

    public String getKickChannel() {
        return kick_channel;
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(RouterChangeEvent event) {
        String userId = event.userId;
        Router<?> r = event.router;
        if (r.getRouteType().equals(Router.RouterType.LOCAL)) {
            sendKickUserMessage2Client(userId, (LocalRouter) r);
        } else {
            sendKickUserMessage2MQ(userId, (RemoteRouter) r);
        }
    }

    /**
     * 发送踢人消息到客户端
     *
     * @param userId 当前用户
     * @param router 本地路由信息
     */
    private void sendKickUserMessage2Client(final String userId, final LocalRouter router) {
        Connection connection = router.getRouteValue();
        SessionContext context = connection.getSessionContext();
        KickUserMessage message = KickUserMessage.build(connection);
        message.deviceId = context.deviceId;
        message.userId = userId;
        message.send(future -> {
            if (future.isSuccess()) {
                Logs.CONN.info("kick local connection success, userId={}, router={}, conn={}", userId, router, connection);
            } else {
                Logs.CONN.warn("kick local connection failure, userId={}, router={}, conn={}", userId, router, connection);
            }
        });
    }

    /**
     * 广播踢人消息到消息中心（MQ）.
     * <p>
     * 有可能目标机器是当前机器，所以要做一次过滤
     * 如果client连续2次链接到同一台机器上就有会出现这中情况
     *
     * @param userId       当前用户
     * @param remoteRouter 用户的路由信息
     */
    private void sendKickUserMessage2MQ(String userId, RemoteRouter remoteRouter) {
        ClientLocation location = remoteRouter.getRouteValue();
        //1.如果目标机器是当前机器，就不要再发送广播了，直接忽略
        if (elionServer.isTargetMachine(location.getHost(), location.getPort())) {
            Logs.CONN.debug("kick remote router in local pc, ignore remote broadcast, userId={}", userId);
            return;
        }

        if (udpGateway) {
            Connection connection = elionServer.getUdpGatewayServer().getConnection();
            GatewayKickUserMessage.build(connection)
                    .setUserId(userId)
                    .setClientType(location.getClientType())
                    .setConnId(location.getConnId())
                    .setDeviceId(location.getDeviceId())
                    .setTargetServer(location.getHost())
                    .setTargetPort(location.getPort())
                    .setRecipient(new InetSocketAddress(location.getHost(), location.getPort()))
                    .sendRaw();
        } else {
            //2.发送广播
            //TODO 远程机器可能不存在，需要确认下redis 那个通道如果机器不存在的话，是否会存在消息积压的问题。
            MQKickRemoteMsg message = new MQKickRemoteMsg()
                    .setUserId(userId)
                    .setClientType(location.getClientType())
                    .setConnId(location.getConnId())
                    .setDeviceId(location.getDeviceId())
                    .setTargetServer(location.getHost())
                    .setTargetPort(location.getPort());
            mqClient.publish(Constants.getKickChannel(location.getHostAndPort()), message);
        }
    }

    /**
     * 处理远程机器发送的踢人广播.
     * <p>
     * 一台机器发送广播所有的机器都能收到，
     * 包括发送广播的机器，所有要做一次过滤
     *
     * @param msg
     */
    public void onReceiveKickRemoteMsg(KickRemoteMsg msg) {
        //1.如果当前机器不是目标机器，直接忽略
        if (!elionServer.isTargetMachine(msg.getTargetServer(), msg.getTargetPort())) {
            Logs.CONN.error("receive kick remote msg, target server error, localIp={}, msg={}", ConfigTools.getLocalIp(), msg);
            return;
        }

        //2.查询本地路由，找到要被踢下线的链接，并删除该本地路由
        String userId = msg.getUserId();
        int clientType = msg.getClientType();
        LocalRouterManager localRouterManager = elionServer.getRouterCenter().getLocalRouterManager();
        LocalRouter localRouter = localRouterManager.lookup(userId, clientType);
        if (localRouter != null) {
            Logs.CONN.info("receive kick remote msg, msg={}", msg);
            if (localRouter.getRouteValue().getId().equals(msg.getConnId())) {//二次校验，防止误杀
                //fix 0.8.1 踢人的时候不再主动删除路由信息，只发踢人消息到客户端，路由信息有客户端主动解绑的时候再处理。
                //2.1删除本地路由信息
                //localRouterManager.unRegister(userId, clientType);
                //2.2发送踢人消息到客户端
                sendKickUserMessage2Client(userId, localRouter);
            } else {
                Logs.CONN.warn("kick router failure target connId not match, localRouter={}, msg={}", localRouter, msg);
            }
        } else {
            Logs.CONN.warn("kick router failure can't find local router, msg={}", msg);
        }
    }

    @Override
    public void receive(String topic, Object message) {
        if (getKickChannel().equals(topic)) {
            KickRemoteMsg msg = Jsons.fromJson(message.toString(), MQKickRemoteMsg.class);
            if (msg != null) {
                onReceiveKickRemoteMsg(msg);
            } else {
                Logs.CONN.warn("receive an error kick message={}", message);
            }
        } else {
            Logs.CONN.warn("receive an error redis channel={}", topic);
        }
    }
}
