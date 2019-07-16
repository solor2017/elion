
package com.hcq.elion.tools.event;

public abstract class EventConsumer {

    public EventConsumer() {
        EventBus.register(this);
    }

}
