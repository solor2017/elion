
package com.hcq.elion.cache.redis.manager;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.CacheManager;
import com.hcq.elion.api.spi.common.CacheManagerFactory;

/**
 *
 *
 */
@Spi(order = 1)
public final class RedisCacheManagerFactory implements CacheManagerFactory {
    @Override
    public CacheManager get() {
        return RedisManager.I;
    }
}
