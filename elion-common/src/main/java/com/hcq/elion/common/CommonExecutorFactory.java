
package com.hcq.elion.common;

import com.hcq.elion.api.spi.common.ExecutorFactory;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.log.Logs;
import com.hcq.elion.tools.thread.NamedPoolThreadFactory;
import com.hcq.elion.tools.thread.pool.DefaultExecutor;
import com.hcq.elion.tools.thread.pool.DumpThreadRejectedHandler;
import com.hcq.elion.tools.thread.pool.ThreadPoolConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.hcq.elion.tools.config.CC.elion.thread.pool.ack_timer;
import static com.hcq.elion.tools.config.CC.elion.thread.pool.push_client;
import static com.hcq.elion.tools.thread.ThreadNames.T_ARK_REQ_TIMER;
import static com.hcq.elion.tools.thread.ThreadNames.T_EVENT_BUS;
import static com.hcq.elion.tools.thread.ThreadNames.T_PUSH_CLIENT_TIMER;

/**
 *
 *
 */
public class CommonExecutorFactory implements ExecutorFactory {
    protected Executor get(ThreadPoolConfig config) {
        String name = config.getName();
        int corePoolSize = config.getCorePoolSize();
        int maxPoolSize = config.getMaxPoolSize();
        int keepAliveSeconds = config.getKeepAliveSeconds();
        BlockingQueue<Runnable> queue = config.getQueue();

        return new DefaultExecutor(corePoolSize
                , maxPoolSize
                , keepAliveSeconds
                , TimeUnit.SECONDS
                , queue
                , new NamedPoolThreadFactory(name)
                , new DumpThreadRejectedHandler(config));
    }

    @Override
    public Executor get(String name) {
        final ThreadPoolConfig config;
        switch (name) {
            case EVENT_BUS:
                config = ThreadPoolConfig
                        .build(T_EVENT_BUS)
                        .setCorePoolSize(CC.elion.thread.pool.event_bus.min)
                        .setMaxPoolSize(CC.elion.thread.pool.event_bus.max)
                        .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(10))
                        .setQueueCapacity(CC.elion.thread.pool.event_bus.queue_size)
                        .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
                break;
            case PUSH_CLIENT: {
                ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(push_client
                        , new NamedPoolThreadFactory(T_PUSH_CLIENT_TIMER), (r, e) -> r.run() // run caller thread
                );
                executor.setRemoveOnCancelPolicy(true);
                return executor;
            }
            case ACK_TIMER: {
                ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(ack_timer,
                        new NamedPoolThreadFactory(T_ARK_REQ_TIMER),
                        (r, e) -> Logs.PUSH.error("one ack context was rejected, context=" + r)
                );
                executor.setRemoveOnCancelPolicy(true);
                return executor;
            }
            default:
                throw new IllegalArgumentException("no executor for " + name);
        }

        return get(config);
    }
}
