
package com.hcq.elion.api.spi.common;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;


public interface JsonFactory extends Factory<Json> {

    static Json create() {
        return SpiLoader.load(JsonFactory.class).get();
    }
}
