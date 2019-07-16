
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.core.ElionServer;

/**
 */
public final class MonitorBoot extends BootJob {

    private final ElionServer elionServer;

    public MonitorBoot(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    @Override
    protected void start() {
        elionServer.getMonitor().start();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        elionServer.getMonitor().stop();
    }
}
