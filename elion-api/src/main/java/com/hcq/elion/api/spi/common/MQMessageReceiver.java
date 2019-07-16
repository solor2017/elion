
package com.hcq.elion.api.spi.common;


public interface MQMessageReceiver {
    void receive(String topic, Object message);
}
