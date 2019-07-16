
package com.hcq.elion.client.push;

import com.hcq.elion.api.push.PushSender;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.client.PusherFactory;

/**
 */
@Spi
public class PushClientFactory implements PusherFactory {
    private volatile PushClient client;

    @Override
    public PushSender get() {
        if (client == null) {
            synchronized (PushClientFactory.class) {
                if (client == null) {
                    client = new PushClient();
                }
            }
        }
        return client;
    }
}
