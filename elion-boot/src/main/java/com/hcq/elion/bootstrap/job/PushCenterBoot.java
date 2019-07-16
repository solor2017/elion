
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.core.ElionServer;

/**
 *
 */
public final class PushCenterBoot extends BootJob {
    private final ElionServer elionServer;

    public PushCenterBoot(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    @Override
    protected void start() {
        elionServer.getPushCenter().start();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        elionServer.getPushCenter().stop();
    }
}
