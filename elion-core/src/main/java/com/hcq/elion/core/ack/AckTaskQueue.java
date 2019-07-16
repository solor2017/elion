
package com.hcq.elion.core.ack;

import com.hcq.elion.api.service.BaseService;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.core.ElionServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class AckTaskQueue extends BaseService {
    private static final int DEFAULT_TIMEOUT = 3000;

    private final Logger logger = LoggerFactory.getLogger(AckTaskQueue.class);

    private final ConcurrentMap<Integer, AckTask> queue = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutor;
    private ElionServer elionServer;

    public AckTaskQueue(ElionServer elionServer) {
        this.elionServer = elionServer;
    }

    public void add(AckTask task, int timeout) {
        queue.put(task.getAckMessageId(), task);
        task.setAckTaskQueue(this);
        task.setFuture(scheduledExecutor.schedule(task,//使用 task.getExecutor() 并没更快
                timeout > 0 ? timeout : DEFAULT_TIMEOUT,
                TimeUnit.MILLISECONDS
        ));

        logger.debug("one ack task add to queue, task={}, timeout={}", task, timeout);
    }

    public AckTask getAndRemove(int sessionId) {
        return queue.remove(sessionId);
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        scheduledExecutor = elionServer.getMonitor().getThreadPoolManager().getAckTimer();
        super.doStart(listener);
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdown();
        }
        super.doStop(listener);
    }
}
