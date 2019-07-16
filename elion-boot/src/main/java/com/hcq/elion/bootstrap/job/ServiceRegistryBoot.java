
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.api.spi.common.ServiceRegistryFactory;
import com.hcq.elion.tools.log.Logs;

/**
 */
public final class ServiceRegistryBoot extends BootJob {

    @Override
    protected void start() {
        Logs.Console.info("init service registry waiting for connected...");
        ServiceRegistryFactory.create().syncStart();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        ServiceRegistryFactory.create().syncStop();
        Logs.Console.info("service registry closed...");
    }
}
