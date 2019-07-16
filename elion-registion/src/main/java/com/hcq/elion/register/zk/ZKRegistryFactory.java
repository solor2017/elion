
package com.hcq.elion.register.zk;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.ServiceRegistryFactory;
import com.hcq.elion.api.srd.ServiceRegistry;

/**
 */
@Spi(order = 1)
public final class ZKRegistryFactory implements ServiceRegistryFactory {
    @Override
    public ServiceRegistry get() {
        return ZKServiceRegistryAndDiscovery.I;
    }
}
