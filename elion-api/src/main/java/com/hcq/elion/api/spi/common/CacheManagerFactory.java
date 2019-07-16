
package com.hcq.elion.api.spi.common;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;


public interface CacheManagerFactory extends Factory<CacheManager> {
    static CacheManager create() {
        return SpiLoader.load(CacheManagerFactory.class).get();
    }
}
