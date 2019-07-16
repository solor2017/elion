
package com.hcq.elion.api.spi.handler;

import com.hcq.elion.api.message.MessageHandler;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.message.MessageHandler;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;

/**
 *
 */
public interface PushHandlerFactory extends Factory<MessageHandler> {
    static MessageHandler create() {
        return SpiLoader.load(PushHandlerFactory.class).get();
    }
}
