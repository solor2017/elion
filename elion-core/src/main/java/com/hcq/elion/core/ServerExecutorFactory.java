
package com.hcq.elion.core;

import com.hcq.elion.api.push.PushException;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.common.CommonExecutorFactory;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.log.Logs;
import com.hcq.elion.tools.thread.NamedPoolThreadFactory;
import com.hcq.elion.tools.thread.pool.ThreadPoolConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.hcq.elion.tools.config.CC.elion.thread.pool.ack_timer;
import static com.hcq.elion.tools.config.CC.elion.thread.pool.push_task;
import static com.hcq.elion.tools.thread.ThreadNames.*;

/**
 * 此线程池可伸缩，线程空闲一定时间后回收，新请求重新创建线程
 */
@Spi(order = 1)
public final class ServerExecutorFactory extends CommonExecutorFactory {

    @Override
    public Executor get(String name) {
        final ThreadPoolConfig config;
        switch (name) {
            case MQ:
                config = ThreadPoolConfig
                        .build(T_MQ)
                        .setCorePoolSize(CC.elion.thread.pool.mq.min)
                        .setMaxPoolSize(CC.elion.thread.pool.mq.max)
                        .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(10))
                        .setQueueCapacity(CC.elion.thread.pool.mq.queue_size)
                        .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
                break;
            case PUSH_TASK:
                return new ScheduledThreadPoolExecutor(push_task, new NamedPoolThreadFactory(T_PUSH_CENTER_TIMER),
                        (r, e) -> {
                            throw new PushException("one push task was rejected. task=" + r);
                        }
                );
            case ACK_TIMER: {
                ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(ack_timer,
                        new NamedPoolThreadFactory(T_ARK_REQ_TIMER),
                        (r, e) -> Logs.PUSH.error("one ack context was rejected, context=" + r)
                );
                executor.setRemoveOnCancelPolicy(true);
                return executor;
            }
            default:
                return super.get(name);
        }

        return get(config);
    }
}
