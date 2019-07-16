
package com.hcq.elion.api.spi.common;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;


public interface MQClientFactory extends Factory<MQClient> {

    static MQClient create() {
        return SpiLoader.load(MQClientFactory.class).get();
    }
}
