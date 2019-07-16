
package com.hcq.elion.api.spi.common;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.srd.ServiceRegistry;


public interface ServiceRegistryFactory extends Factory<ServiceRegistry> {
    static ServiceRegistry create() {
        return SpiLoader.load(ServiceRegistryFactory.class).get();
    }
}
