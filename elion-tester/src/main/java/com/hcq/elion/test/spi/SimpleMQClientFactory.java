
package com.hcq.elion.test.spi;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.MQClient;
import com.hcq.elion.api.spi.common.MQMessageReceiver;

/**
 */
@Spi(order = 2)
public final class SimpleMQClientFactory implements com.hcq.elion.api.spi.common.MQClientFactory {
    private MQClient mqClient = new MQClient() {
        @Override
        public void subscribe(String topic, MQMessageReceiver receiver) {
            System.err.println("subscribe " + topic);
        }

        @Override
        public void publish(String topic, Object message) {
            System.err.println("publish " + topic + " " + message);
        }
    };

    @Override
    public MQClient get() {
        return mqClient;
    }
}
