
package com.hcq.elion.test.spi;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.ServiceDiscoveryFactory;
import com.hcq.elion.api.srd.ServiceDiscovery;

/**
 */
@Spi(order = 2)
public final class SimpleDiscoveryFactory implements ServiceDiscoveryFactory {
    @Override
    public ServiceDiscovery get() {
        return FileSrd.I;
    }
}
