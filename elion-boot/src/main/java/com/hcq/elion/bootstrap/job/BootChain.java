
package com.hcq.elion.bootstrap.job;

import com.hcq.elion.api.event.ServerShutdownEvent;
import com.hcq.elion.api.event.ServerStartupEvent;
import com.hcq.elion.api.spi.core.ServerEventListenerFactory;
import com.hcq.elion.tools.event.EventBus;
import com.hcq.elion.tools.log.Logs;

import java.util.function.Supplier;

/**
 */
public final class BootChain {
    private final BootJob boot = new BootJob() {
        {
            ServerEventListenerFactory.create();// 初始化服务监听
        }

        @Override
        protected void start() {
            Logs.Console.info("bootstrap chain starting...");
            startNext();
        }

        @Override
        protected void stop() {
            stopNext();
            Logs.Console.info("bootstrap chain stopped.");
            Logs.Console.info("===================================================================");
            Logs.Console.info("====================ELION SERVER STOPPED SUCCESS===================");
            Logs.Console.info("===================================================================");
        }
    };

    private BootJob last = boot;

    public void start() {
        boot.start();
    }

    public void stop() {
        boot.stop();
    }

    public static BootChain chain() {
        return new BootChain();
    }

    public BootChain boot() {
        return this;
    }

    public void end() {
        setNext(new BootJob() {
            @Override
            protected void start() {
                EventBus.post(new ServerStartupEvent());
                Logs.Console.info("bootstrap chain started.");
                Logs.Console.info("===================================================================");
                Logs.Console.info("====================ELION SERVER START SUCCESS=====================");
                Logs.Console.info("===================================================================");
            }

            @Override
            protected void stop() {
                Logs.Console.info("bootstrap chain stopping...");
                EventBus.post(new ServerShutdownEvent());
            }

            @Override
            protected String getName() {
                return "LastBoot";
            }
        });
    }

    public BootChain setNext(BootJob bootJob) {
        this.last = last.setNext(bootJob);
        return this;
    }

    public BootChain setNext(Supplier<BootJob> next, boolean enabled) {
        if (enabled) {
            return setNext(next.get());
        }
        return this;
    }
}
