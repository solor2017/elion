package com.hcq.elion.core.push;

import com.hcq.elion.api.spi.push.IPushMessage;
import com.hcq.elion.core.ack.AckCallback;
import com.hcq.elion.core.ack.AckTask;
import com.hcq.elion.tools.common.TimeLine;
import com.hcq.elion.tools.log.Logs;

public final class PushAckCallback implements AckCallback {
    private final IPushMessage message;
    private final TimeLine timeLine;
    private final PushCenter pushCenter;

    public PushAckCallback(IPushMessage message, TimeLine timeLine, PushCenter pushCenter) {
        this.message = message;
        this.timeLine = timeLine;
        this.pushCenter = pushCenter;
    }

    @Override
    public void onSuccess(AckTask task) {
        pushCenter.getPushListener().onAckSuccess(message, timeLine.successEnd().getTimePoints());
        Logs.PUSH.info("[SingleUserPush] client ack success, timeLine={}, task={}", timeLine, task);
    }

    @Override
    public void onTimeout(AckTask task) {
        pushCenter.getPushListener().onTimeout(message, timeLine.timeoutEnd().getTimePoints());
        Logs.PUSH.warn("[SingleUserPush] client ack timeout, timeLine={}, task={}", timeLine, task);
    }
}