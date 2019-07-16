
package com.hcq.elion.core.mq;

import com.hcq.elion.api.ElionContext;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.push.PushListener;
import com.hcq.elion.api.spi.push.PushListenerFactory;
import com.hcq.elion.core.ElionServer;


@Spi(order = 2)
public final class MQPushListener implements PushListener<MQPushMessage>, PushListenerFactory<MQPushMessage> {
    private final MQClient mqClient = new MQClient();

    @Override
    public void init(ElionContext context) {
        mqClient.init();
        MQMessageReceiver.subscribe(mqClient, ((ElionServer) context).getPushCenter());
    }


    @Override
    public void onSuccess(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[success/queue]
        mqClient.publish("/elion/push/success", message);
    }

    @Override
    public void onAckSuccess(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[success/queue]
        mqClient.publish("/elion/push/success", message);
    }

    @Override
    public void onBroadcastComplete(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[broadcast/finish/queue]
        mqClient.publish("/elion/push/broadcast_finish", message);
    }

    @Override
    public void onFailure(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[failure/queue], client can retry
        mqClient.publish("/elion/push/failure", message);
    }

    @Override
    public void onOffline(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[offline/queue], client persist offline message to db
        mqClient.publish("/elion/push/offline", message);
    }

    @Override
    public void onRedirect(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[route/change/queue], client should be try again
        mqClient.publish("/elion/push/route_change", message);
    }

    @Override
    public void onTimeout(MQPushMessage message, Object[] timePoints) {
        //publish messageId to mq:[ack/timeout/queue], client can retry
        mqClient.publish("/elion/push/ack_timeout", message);
    }

    @Override
    public PushListener<MQPushMessage> get() {
        return this;
    }
}
