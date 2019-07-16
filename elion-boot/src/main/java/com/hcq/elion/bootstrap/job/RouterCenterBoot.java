
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.core.ElionServer;

/**
 *
 */
public final class RouterCenterBoot extends BootJob {
    private final ElionServer elionServer;

    public RouterCenterBoot(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    @Override
    protected void start() {
        elionServer.getRouterCenter().start();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        elionServer.getRouterCenter().stop();
    }
}
