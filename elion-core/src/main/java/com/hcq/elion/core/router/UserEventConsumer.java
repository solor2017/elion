
package com.hcq.elion.core.router;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.hcq.elion.api.event.UserOfflineEvent;
import com.hcq.elion.api.event.UserOnlineEvent;
import com.hcq.elion.api.spi.common.MQClient;
import com.hcq.elion.api.spi.common.MQClientFactory;
import com.hcq.elion.common.router.RemoteRouterManager;
import com.hcq.elion.common.user.UserManager;
import com.hcq.elion.tools.event.EventConsumer;

import static com.hcq.elion.api.event.Topics.OFFLINE_CHANNEL;
import static com.hcq.elion.api.event.Topics.ONLINE_CHANNEL;


public final class UserEventConsumer extends EventConsumer {

    private final MQClient mqClient = MQClientFactory.create();

    private final UserManager userManager;

    public UserEventConsumer(RemoteRouterManager remoteRouterManager) {
        this.userManager = new UserManager(remoteRouterManager);
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(UserOnlineEvent event) {
        userManager.addToOnlineList(event.getUserId());
        mqClient.publish(ONLINE_CHANNEL, event.getUserId());
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(UserOfflineEvent event) {
        userManager.remFormOnlineList(event.getUserId());
        mqClient.publish(OFFLINE_CHANNEL, event.getUserId());
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
