
package com.hcq.elion.client.push;

import com.hcq.elion.api.service.BaseService;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.client.ElionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 */
public class PushRequestBus extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(PushRequestBus.class);
    private final Map<Integer, PushRequest> reqQueue = new ConcurrentHashMap<>(1024);
    private ScheduledExecutorService scheduledExecutor;
    private final ElionClient elionClient;

    public PushRequestBus(ElionClient elionClient) {
        this.elionClient = elionClient;
    }

    public Future<?> put(int sessionId, PushRequest request) {
        reqQueue.put(sessionId, request);
        return scheduledExecutor.schedule(request, request.getTimeout(), TimeUnit.MILLISECONDS);
    }

    public PushRequest getAndRemove(int sessionId) {
        return reqQueue.remove(sessionId);
    }

    public void asyncCall(Runnable runnable) {
        scheduledExecutor.execute(runnable);
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        scheduledExecutor = elionClient.getThreadPoolManager().getPushClientTimer();
        listener.onSuccess();
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdown();
        }
        listener.onSuccess();
    }
}
