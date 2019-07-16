
package com.hcq.elion.api;

import com.hcq.elion.api.common.Monitor;
import com.hcq.elion.api.spi.common.CacheManager;
import com.hcq.elion.api.spi.common.MQClient;
import com.hcq.elion.api.srd.ServiceDiscovery;
import com.hcq.elion.api.srd.ServiceRegistry;


public interface ElionContext {

    Monitor getMonitor();

    ServiceDiscovery getDiscovery();

    ServiceRegistry getRegistry();

    CacheManager getCacheManager();

    MQClient getMQClient();

}
