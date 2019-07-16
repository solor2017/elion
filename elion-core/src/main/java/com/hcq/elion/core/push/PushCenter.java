
package com.hcq.elion.core.push;

import com.hcq.elion.api.service.BaseService;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.api.spi.push.IPushMessage;
import com.hcq.elion.api.spi.push.MessagePusher;
import com.hcq.elion.api.spi.push.PushListener;
import com.hcq.elion.api.spi.push.PushListenerFactory;
import com.hcq.elion.common.qps.FastFlowControl;
import com.hcq.elion.common.qps.FlowControl;
import com.hcq.elion.common.qps.GlobalFlowControl;
import com.hcq.elion.common.qps.RedisFlowControl;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.core.ack.AckTaskQueue;
import com.hcq.elion.monitor.jmx.MBeanRegistry;
import com.hcq.elion.monitor.jmx.mxbean.PushCenterBean;
import com.hcq.elion.tools.config.CC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public final class PushCenter extends BaseService implements MessagePusher {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GlobalFlowControl globalFlowControl = new GlobalFlowControl(
            CC.elion.push.flow_control.global.limit,
            CC.elion.push.flow_control.global.max,
            CC.elion.push.flow_control.global.duration
    );

    private final AtomicLong taskNum = new AtomicLong();

    private final AckTaskQueue ackTaskQueue;

    private final ElionServer elionServer;

    private PushListener<IPushMessage> pushListener;

    private PushTaskExecutor executor;


    public PushCenter(ElionServer elionServer) {
        this.elionServer = elionServer;
        this.ackTaskQueue = new AckTaskQueue(elionServer);
    }

    @Override
    public void push(IPushMessage message) {
        if (message.isBroadcast()) {
            FlowControl flowControl = (message.getTaskId() == null)
                    ? new FastFlowControl(CC.elion.push.flow_control.broadcast.limit,
                    CC.elion.push.flow_control.broadcast.max,
                    CC.elion.push.flow_control.broadcast.duration)
                    : new RedisFlowControl(message.getTaskId(),
                    CC.elion.push.flow_control.broadcast.max);
            addTask(new BroadcastPushTask(elionServer, message, flowControl));
        } else {
            addTask(new SingleUserPushTask(elionServer, message, globalFlowControl));
        }
    }

    public void addTask(PushTask task) {
        executor.addTask(task);
        logger.debug("add new task to push center, count={}, task={}", taskNum.incrementAndGet(), task);
    }

    public void delayTask(long delay, PushTask task) {
        executor.delayTask(delay, task);
        logger.debug("delay task to push center, count={}, task={}", taskNum.incrementAndGet(), task);
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        this.pushListener = PushListenerFactory.create();
        this.pushListener.init(elionServer);

        if (CC.elion.net.udpGateway() || CC.elion.thread.pool.push_task > 0) {
            executor = new CustomJDKExecutor(elionServer.getMonitor().getThreadPoolManager().getPushTaskTimer());
        } else {//实际情况使用EventLoo并没有更快，还有待测试
            executor = new NettyEventLoopExecutor();
        }

        MBeanRegistry.getInstance().register(new PushCenterBean(taskNum), null);
        ackTaskQueue.start();
        logger.info("push center start success");
        listener.onSuccess();
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        executor.shutdown();
        ackTaskQueue.stop();
        logger.info("push center stop success");
        listener.onSuccess();
    }

    public PushListener<IPushMessage> getPushListener() {
        return pushListener;
    }

    public AckTaskQueue getAckTaskQueue() {
        return ackTaskQueue;
    }

    /**
     * TCP 模式直接使用GatewayServer work 线程池
     */
    private static class NettyEventLoopExecutor implements PushTaskExecutor {

        @Override
        public void shutdown() {
        }

        @Override
        public void addTask(PushTask task) {
            task.getExecutor().execute(task);
        }

        @Override
        public void delayTask(long delay, PushTask task) {
            task.getExecutor().schedule(task, delay, TimeUnit.NANOSECONDS);
        }
    }


    /**
     * UDP 模式使用自定义线程池
     */
    private static class CustomJDKExecutor implements PushTaskExecutor {
        private final ScheduledExecutorService executorService;

        private CustomJDKExecutor(ScheduledExecutorService executorService) {
            this.executorService = executorService;
        }

        @Override
        public void shutdown() {
            executorService.shutdown();
        }

        @Override
        public void addTask(PushTask task) {
            executorService.execute(task);
        }

        @Override
        public void delayTask(long delay, PushTask task) {
            executorService.schedule(task, delay, TimeUnit.NANOSECONDS);
        }
    }

    private interface PushTaskExecutor {

        void shutdown();

        void addTask(PushTask task);

        void delayTask(long delay, PushTask task);
    }
}
