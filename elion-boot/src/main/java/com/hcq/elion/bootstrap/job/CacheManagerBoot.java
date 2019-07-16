
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.api.spi.common.CacheManagerFactory;

/**
 */
public final class CacheManagerBoot extends BootJob {

    @Override
    protected void start() {
        CacheManagerFactory.create().init();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        CacheManagerFactory.create().destroy();
    }
}
