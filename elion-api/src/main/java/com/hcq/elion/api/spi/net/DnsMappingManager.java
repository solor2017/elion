
package com.hcq.elion.api.spi.net;

import com.hcq.elion.api.service.Service;
import com.hcq.elion.api.spi.SpiLoader;

/**
 *
 */
public interface DnsMappingManager extends Service {

    static DnsMappingManager create() {
        return SpiLoader.load(DnsMappingManager.class);
    }

    DnsMapping lookup(String origin);
}
