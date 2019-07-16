
package com.hcq.elion.core.mq;

import com.hcq.elion.core.push.PushCenter;
import com.hcq.elion.tools.Utils;
import com.hcq.elion.tools.config.ConfigTools;

import java.util.Collection;


public final class MQMessageReceiver {

    private final static String TOPIC = "/elion/push/" + ConfigTools.getLocalIp();

    private final MQClient mqClient;

    private PushCenter pushCenter;

    public static void subscribe(MQClient mqClient, PushCenter pushCenter) {
        MQMessageReceiver receiver = new MQMessageReceiver(mqClient, pushCenter);
        mqClient.subscribe(TOPIC, receiver);
        receiver.fetchFormMQ();
    }

    public MQMessageReceiver(MQClient mqClient, PushCenter pushCenter) {
        this.mqClient = mqClient;
        this.pushCenter = pushCenter;
    }

    public void onMessage(MQPushMessage message) {
        pushCenter.push(message);
    }

    public void fetchFormMQ() {
        Utils.newThread("mq-push", this::dispatch);
    }

    private void dispatch() {
        try {
            while (true) {
                Collection<MQPushMessage> message = mqClient.take(TOPIC);
                if (message == null || message.isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                message.forEach(this::onMessage);
            }
        } catch (InterruptedException e) {
            this.dispatch();
        }
    }
}
