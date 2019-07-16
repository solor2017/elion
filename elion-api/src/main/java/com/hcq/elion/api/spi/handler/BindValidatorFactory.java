
package com.hcq.elion.api.spi.handler;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;


public interface BindValidatorFactory extends Factory<BindValidator> {
    static BindValidator create() {
        return SpiLoader.load(BindValidatorFactory.class).get();
    }
}
