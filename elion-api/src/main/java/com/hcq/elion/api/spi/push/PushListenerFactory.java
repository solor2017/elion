

package com.hcq.elion.api.spi.push;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;

/**
 *
 *
 */
public interface PushListenerFactory<M extends IPushMessage> extends Factory<PushListener<M>> {

    @SuppressWarnings("unchecked")
    static <M extends IPushMessage> PushListener<M> create() {
        return (PushListener<M>) SpiLoader.load(PushListenerFactory.class).get();
    }
}
