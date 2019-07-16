
package com.hcq.elion.tools.thread.pool;

import com.hcq.elion.tools.Utils;
import com.hcq.elion.tools.common.JVMUtil;
import com.hcq.elion.tools.config.CC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.hcq.elion.tools.thread.pool.ThreadPoolConfig.REJECTED_POLICY_ABORT;
import static com.hcq.elion.tools.thread.pool.ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS;

public final class DumpThreadRejectedHandler implements RejectedExecutionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(DumpThreadRejectedHandler.class);

    private volatile boolean dumping = false;

    private static final String DUMP_DIR = CC.elion.monitor.dump_dir;

    private final ThreadPoolConfig poolConfig;

    private final int rejectedPolicy;

    public DumpThreadRejectedHandler(ThreadPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        this.rejectedPolicy = poolConfig.getRejectedPolicy();
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        LOGGER.warn("one task rejected, poolConfig={}, poolInfo={}", poolConfig, Utils.getPoolInfo(e));
        if (!dumping) {
            dumping = true;
            dumpJVMInfo();
        }

        if (rejectedPolicy == REJECTED_POLICY_ABORT) {
            throw new RejectedExecutionException("one task rejected, pool=" + poolConfig.getName());
        } else if (rejectedPolicy == REJECTED_POLICY_CALLER_RUNS) {
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }

    private void dumpJVMInfo() {
        LOGGER.info("start dump jvm info");
        JVMUtil.dumpJstack(DUMP_DIR + "/" + poolConfig.getName());
        LOGGER.info("end dump jvm info");
    }
}

