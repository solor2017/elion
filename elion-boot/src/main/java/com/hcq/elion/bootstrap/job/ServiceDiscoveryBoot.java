
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.api.spi.common.ServiceDiscoveryFactory;
import com.hcq.elion.tools.log.Logs;

/**
 */
public final class ServiceDiscoveryBoot extends BootJob {

    @Override
    protected void start() {
        Logs.Console.info("init service discovery waiting for connected...");
        ServiceDiscoveryFactory.create().syncStart();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        ServiceDiscoveryFactory.create().syncStop();
        Logs.Console.info("service discovery closed...");
    }
}
