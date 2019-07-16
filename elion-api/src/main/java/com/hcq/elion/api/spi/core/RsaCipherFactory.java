
package com.hcq.elion.api.spi.core;

import com.hcq.elion.api.connection.Cipher;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;


public interface RsaCipherFactory extends Factory<Cipher> {
    static Cipher create() {
        return SpiLoader.load(RsaCipherFactory.class).get();
    }
}
