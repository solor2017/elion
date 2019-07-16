
package com.hcq.elion.test.spi;

import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.common.ServiceRegistryFactory;
import com.hcq.elion.api.srd.ServiceRegistry;

/**
 */
@Spi(order = 2)
public final class SimpleRegistryFactory implements ServiceRegistryFactory {
    @Override
    public ServiceRegistry get() {
        return FileSrd.I;
    }
}
