
package com.hcq.elion.register.zk;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.ServiceDiscoveryFactory;
import com.hcq.elion.api.srd.ServiceDiscovery;

/**
 *
 */
@Spi(order = 1)
public final class ZKDiscoveryFactory implements ServiceDiscoveryFactory {
    @Override
    public ServiceDiscovery get() {
        return ZKServiceRegistryAndDiscovery.I;
    }
}
