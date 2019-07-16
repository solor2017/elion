
package com.hcq.elion.core.mq;

import com.hcq.elion.api.common.Condition;
import com.hcq.elion.api.spi.push.IPushMessage;


public final class MQPushMessage implements IPushMessage {

    @Override
    public boolean isBroadcast() {
        return false;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public int getClientType() {
        return 0;
    }

    @Override
    public byte[] getContent() {
        return new byte[0];
    }

    @Override
    public boolean isNeedAck() {
        return false;
    }

    @Override
    public byte getFlags() {
        return 0;
    }

    @Override
    public int getTimeoutMills() {
        return 0;
    }

    @Override
    public String getTaskId() {
        return null;
    }

    @Override
    public Condition getCondition() {
        return null;
    }
}
