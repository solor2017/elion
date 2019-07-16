
package com.hcq.elion.common.message;

import com.hcq.elion.api.Constants;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.JsonPacket;
import com.hcq.elion.api.protocol.Packet;
import io.netty.channel.ChannelFutureListener;

import java.util.Collections;
import java.util.Map;

import static com.hcq.elion.api.protocol.Command.PUSH;

/**
 */
public final class PushMessage extends BaseMessage {

    public byte[] content;

    public PushMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    public static PushMessage build(Connection connection) {
        if (connection.getSessionContext().isSecurity()) {
            return new PushMessage(new Packet(PUSH, genSessionId()), connection);
        } else {
            return new PushMessage(new JsonPacket(PUSH, genSessionId()), connection);
        }
    }

    @Override
    public void decode(byte[] body) {
        content = body;
    }

    @Override
    public byte[] encode() {
        return content;
    }

    @Override
    public void decodeJsonBody(Map<String, Object> body) {
        String content = (String) body.get("content");
        if (content != null) {
            this.content = content.getBytes(Constants.UTF_8);
        }
    }

    @Override
    public Map<String, Object> encodeJsonBody() {
        if (content != null) {
            return Collections.singletonMap("content", new String(content, Constants.UTF_8));
        }
        return null;
    }

    public boolean autoAck() {
        return packet.hasFlag(Packet.FLAG_AUTO_ACK);
    }

    public boolean needAck() {
        return packet.hasFlag(Packet.FLAG_BIZ_ACK) || packet.hasFlag(Packet.FLAG_AUTO_ACK);
    }

    public PushMessage setContent(byte[] content) {
        this.content = content;
        return this;
    }



    @Override
    public void send(ChannelFutureListener listener) {
        super.send(listener);
        this.content = null;//释放内存
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "content='" + content.length + '\'' +
                ", packet=" + packet +
                '}';
    }
}
