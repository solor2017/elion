
package com.hcq.elion.api.spi.push;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;

/**
 *
 *
 */
public interface MessagePusherFactory extends Factory<MessagePusher> {

    static MessagePusher create() {
        return SpiLoader.load(MessagePusherFactory.class).get();
    }
}
