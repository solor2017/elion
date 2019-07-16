
package com.hcq.elion.core.handler;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.common.handler.BaseMessageHandler;
import com.hcq.elion.common.message.ErrorMessage;
import com.hcq.elion.common.message.FastConnectMessage;
import com.hcq.elion.common.message.FastConnectOkMessage;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.core.session.ReusableSession;
import com.hcq.elion.core.session.ReusableSessionManager;
import com.hcq.elion.tools.common.Profiler;
import com.hcq.elion.tools.config.ConfigTools;
import com.hcq.elion.tools.log.Logs;

import static com.hcq.elion.common.ErrorCode.INVALID_DEVICE;
import static com.hcq.elion.common.ErrorCode.SESSION_EXPIRED;


public final class FastConnectHandler extends BaseMessageHandler<FastConnectMessage> {
    private final ReusableSessionManager reusableSessionManager;

    public FastConnectHandler(ElionServer elionServer) {
        this.reusableSessionManager = elionServer.getReusableSessionManager();
    }

    @Override
    public FastConnectMessage decode(Packet packet, Connection connection) {
        return new FastConnectMessage(packet, connection);
    }

    @Override
    public void handle(FastConnectMessage message) {
        //从缓存中心查询session
        Profiler.enter("time cost on [query session]");
        ReusableSession session = reusableSessionManager.querySession(message.sessionId);
        Profiler.release();
        if (session == null) {
            //1.没查到说明session已经失效了
            ErrorMessage.from(message).setErrorCode(SESSION_EXPIRED).send();
            Logs.CONN.warn("fast connect failure, session is expired, sessionId={}, deviceId={}, conn={}"
                    , message.sessionId, message.deviceId, message.getConnection().getChannel());
        } else if (!session.context.deviceId.equals(message.deviceId)) {
            //2.非法的设备, 当前设备不是上次生成session时的设备
            ErrorMessage.from(message).setErrorCode(INVALID_DEVICE).send();
            Logs.CONN.warn("fast connect failure, not the same device, deviceId={}, session={}, conn={}"
                    , message.deviceId, session.context, message.getConnection().getChannel());
        } else {
            //3.校验成功，重新计算心跳，完成快速重连
            int heartbeat = ConfigTools.getHeartbeat(message.minHeartbeat, message.maxHeartbeat);
            session.context.setHeartbeat(heartbeat);

            Profiler.enter("time cost on [send FastConnectOkMessage]");
            FastConnectOkMessage
                    .from(message)
                    .setHeartbeat(heartbeat)
                    .sendRaw(f -> {
                        if (f.isSuccess()) {
                            //4. 恢复缓存的会话信息(包含会话密钥等)
                            message.getConnection().setSessionContext(session.context);
                            Logs.CONN.info("fast connect success, session={}, conn={}", session.context, message.getConnection().getChannel());
                        } else {
                            Logs.CONN.info("fast connect failure, session={}, conn={}", session.context, message.getConnection().getChannel());
                        }
                    });

            Profiler.release();
        }
    }
}
