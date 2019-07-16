
package com.hcq.elion.test.spi;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.CacheManager;
import com.hcq.elion.api.spi.common.CacheManagerFactory;

/**
 */
@Spi(order = 2)
public final class SimpleCacheMangerFactory implements CacheManagerFactory {
    @Override
    public CacheManager get() {
        return FileCacheManger.I;
    }
}
