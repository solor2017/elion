
package com.hcq.elion.api.spi.core;

import com.hcq.elion.api.common.ServerEventListener;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.spi.Factory;


public interface ServerEventListenerFactory extends Factory<ServerEventListener> {
    static ServerEventListener create() {
        return SpiLoader.load(ServerEventListenerFactory.class).get();
    }
}
