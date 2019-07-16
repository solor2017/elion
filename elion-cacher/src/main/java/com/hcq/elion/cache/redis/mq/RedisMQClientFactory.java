
package com.hcq.elion.cache.redis.mq;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.MQClient;
import com.hcq.elion.api.spi.common.MQClientFactory;

/**
 *
 */
@Spi(order = 1)
public final class RedisMQClientFactory implements MQClientFactory {
    private ListenerDispatcher listenerDispatcher = new ListenerDispatcher();

    @Override
    public MQClient get() {
        return listenerDispatcher;
    }
}
