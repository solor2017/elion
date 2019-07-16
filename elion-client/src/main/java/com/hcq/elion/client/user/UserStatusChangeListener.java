
package com.hcq.elion.client.user;

import com.hcq.elion.api.spi.common.MQClientFactory;
import com.hcq.elion.api.spi.common.MQMessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hcq.elion.api.event.Topics.OFFLINE_CHANNEL;
import static com.hcq.elion.api.event.Topics.ONLINE_CHANNEL;

/**
 */
public class UserStatusChangeListener implements MQMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatusChangeListener.class);

    //只需要一台机器注册online、offline 消息通道
    public UserStatusChangeListener() {
        MQClientFactory.create().subscribe(ONLINE_CHANNEL, this);
        MQClientFactory.create().subscribe(OFFLINE_CHANNEL, this);
    }

    @Override
    public void receive(String channel, Object message) {

    }
}
