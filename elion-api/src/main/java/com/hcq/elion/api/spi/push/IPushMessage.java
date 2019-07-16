

package com.hcq.elion.api.spi.push;

import com.hcq.elion.api.common.Condition;

/**
 *
 *
 */
public interface IPushMessage {

    boolean isBroadcast();

    String getUserId();

    int getClientType();

    byte[] getContent();

    boolean isNeedAck();

    byte getFlags();

    int getTimeoutMills();

    default String getTaskId() {
        return null;
    }

    default Condition getCondition() {
        return null;
    }

    default void finalized() {

    }

}
