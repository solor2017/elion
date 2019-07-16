
package com.hcq.elion.api.spi.client;

import com.hcq.elion.api.push.PushSender;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;


public interface PusherFactory extends Factory<PushSender> {
    static PushSender create() {
        return SpiLoader.load(PusherFactory.class).get();
    }
}
