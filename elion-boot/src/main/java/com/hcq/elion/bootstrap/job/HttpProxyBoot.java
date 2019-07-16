
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.api.spi.net.DnsMappingManager;
import com.hcq.elion.core.ElionServer;

/**
 */
public final class HttpProxyBoot extends BootJob {

    private final ElionServer elionServer;

    public HttpProxyBoot(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    @Override
    protected void start() {
        elionServer.getHttpClient().syncStart();
        DnsMappingManager.create().start();

        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        elionServer.getHttpClient().syncStop();
        DnsMappingManager.create().stop();
    }
}
