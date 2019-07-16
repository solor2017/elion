
package com.hcq.elion.common.message;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import io.netty.buffer.ByteBuf;

import static com.hcq.elion.api.protocol.Command.FAST_CONNECT;

/**
 */
public final class FastConnectMessage extends ByteBufMessage {
    public String sessionId;
    public String deviceId;
    public int minHeartbeat;
    public int maxHeartbeat;

    public FastConnectMessage(Connection connection) {
        super(new Packet(FAST_CONNECT, genSessionId()), connection);
    }

    public FastConnectMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    @Override
    public void decode(ByteBuf body) {
        sessionId = decodeString(body);
        deviceId = decodeString(body);
        minHeartbeat = decodeInt(body);
        maxHeartbeat = decodeInt(body);
    }

    @Override
    public void encode(ByteBuf body) {
        encodeString(body, sessionId);
        encodeString(body, deviceId);
        encodeInt(body, minHeartbeat);
        encodeInt(body, maxHeartbeat);
    }

    @Override
    public String toString() {
        return "FastConnectMessage{" +
                "deviceId='" + deviceId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", minHeartbeat=" + minHeartbeat +
                ", maxHeartbeat=" + maxHeartbeat +
                ", packet=" + packet +
                '}';
    }
}
