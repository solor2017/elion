
package com.hcq.elion.common;

import com.hcq.elion.api.message.MessageHandler;
import com.hcq.elion.api.message.PacketReceiver;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.protocol.Packet;
import com.hcq.elion.common.message.ErrorMessage;
import com.hcq.elion.tools.common.Profiler;
import com.hcq.elion.tools.log.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 */
public final class MessageDispatcher implements PacketReceiver {
    public static final int POLICY_REJECT = 2;
    public static final int POLICY_LOG = 1;
    public static final int POLICY_IGNORE = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);
    private final Map<Byte, MessageHandler> handlers = new HashMap<>();
    private final int unsupportedPolicy;

    public MessageDispatcher() {
        unsupportedPolicy = POLICY_REJECT;
    }

    public MessageDispatcher(int unsupportedPolicy) {
        this.unsupportedPolicy = unsupportedPolicy;
    }

    public void register(Command command, MessageHandler handler) {
        handlers.put(command.cmd, handler);
    }

    public void register(Command command, Supplier<MessageHandler> handlerSupplier, boolean enabled) {
        if (enabled && !handlers.containsKey(command.cmd)) {
            register(command, handlerSupplier.get());
        }
    }

    public void register(Command command, Supplier<MessageHandler> handlerSupplier) {
        this.register(command, handlerSupplier, true);
    }

    public MessageHandler unRegister(Command command) {
        return handlers.remove(command.cmd);
    }

    @Override
    public void onReceive(Packet packet, Connection connection) {
        MessageHandler handler = handlers.get(packet.cmd);
        if (handler != null) {
            Profiler.enter("time cost on [dispatch]");
            try {
                handler.handle(packet, connection);
            } catch (Throwable throwable) {
                LOGGER.error("dispatch message ex, packet={}, connect={}, body={}"
                        , packet, connection, Arrays.toString(packet.body), throwable);
                Logs.CONN.error("dispatch message ex, packet={}, connect={}, body={}, error={}"
                        , packet, connection, Arrays.toString(packet.body), throwable.getMessage());
                ErrorMessage
                        .from(packet, connection)
                        .setErrorCode(ErrorCode.DISPATCH_ERROR)
                        .close();
            } finally {
                Profiler.release();
            }
        } else {
            if (unsupportedPolicy > POLICY_IGNORE) {
                Logs.CONN.error("dispatch message failure, cmd={} unsupported, packet={}, connect={}, body={}"
                        , Command.toCMD(packet.cmd), packet, connection);
                if (unsupportedPolicy == POLICY_REJECT) {
                    ErrorMessage
                            .from(packet, connection)
                            .setErrorCode(ErrorCode.UNSUPPORTED_CMD)
                            .close();
                }
            }
        }
    }
}
