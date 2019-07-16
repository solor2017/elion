
package com.hcq.elion.api.spi.common;

import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.srd.ServiceDiscovery;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.srd.ServiceDiscovery;


public interface ServiceDiscoveryFactory extends Factory<ServiceDiscovery> {
    static ServiceDiscovery create() {
        return SpiLoader.load(ServiceDiscoveryFactory.class).get();
    }
}
