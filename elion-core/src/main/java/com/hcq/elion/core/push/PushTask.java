
package com.hcq.elion.core.push;

import java.util.concurrent.ScheduledExecutorService;


public interface PushTask extends Runnable {
    ScheduledExecutorService getExecutor();
}
