
package com.hcq.elion.api.spi.common;

import com.hcq.elion.api.spi.Plugin;
import com.hcq.elion.api.spi.Plugin;


public interface MQClient extends Plugin {

    void subscribe(String topic, MQMessageReceiver receiver);

    void publish(String topic, Object message);
}
