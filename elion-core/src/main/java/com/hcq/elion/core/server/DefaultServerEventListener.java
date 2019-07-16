
package com.hcq.elion.core.server;

import com.hcq.elion.api.common.ServerEventListener;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.core.ServerEventListenerFactory;


@Spi(order = 1)
public final class DefaultServerEventListener implements ServerEventListener, ServerEventListenerFactory {

    @Override
    public ServerEventListener get() {
        return this;
    }
}
