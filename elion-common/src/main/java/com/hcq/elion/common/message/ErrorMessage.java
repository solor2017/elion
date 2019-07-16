
package com.hcq.elion.common.message;

import com.hcq.elion.common.ErrorCode;
import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;

import java.util.HashMap;
import java.util.Map;

import static com.hcq.elion.api.protocol.Command.ERROR;

/**
 */
public final class ErrorMessage extends ByteBufMessage {
    public byte cmd;
    public byte code;
    public String reason;
    public String data;

    public ErrorMessage(byte cmd, Packet message, Connection connection) {
        super(message, connection);
        this.cmd = cmd;
    }

    public ErrorMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    @Override
    public void decode(ByteBuf body) {
        cmd = decodeByte(body);
        code = decodeByte(body);
        reason = decodeString(body);
        data = decodeString(body);
    }

    @Override
    public void encode(ByteBuf body) {
        encodeByte(body, cmd);
        encodeByte(body, code);
        encodeString(body, reason);
        encodeString(body, data);
    }

    @Override
    protected Map<String, Object> encodeJsonBody() {
        Map<String, Object> body = new HashMap<>(4);
        if (cmd > 0) body.put("cmd", cmd);
        if (code > 0) body.put("code", code);
        if (reason != null) body.put("reason", reason);
        if (data != null) body.put("data", data);
        return body;
    }

    public static ErrorMessage from(BaseMessage src) {
        return new ErrorMessage(src.packet.cmd, src.packet.response(ERROR), src.connection);
    }

    public static ErrorMessage from(Packet src, Connection connection) {
        return new ErrorMessage(src.cmd, src.response(ERROR), connection);
    }


    public ErrorMessage setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ErrorMessage setData(String data) {
        this.data = data;
        return this;
    }

    public ErrorMessage setErrorCode(ErrorCode code) {
        this.code = code.errorCode;
        this.reason = code.errorMsg;
        return this;
    }

    @Override
    public void send() {
        super.sendRaw();
    }

    @Override
    public void close() {
        sendRaw(ChannelFutureListener.CLOSE);
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "reason='" + reason + '\'' +
                ", code=" + code +
                ", data=" + data +
                ", packet=" + packet +
                '}';
    }
}
