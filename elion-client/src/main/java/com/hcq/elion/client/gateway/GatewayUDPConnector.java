
package com.hcq.elion.client.gateway;

import com.hcq.elion.api.connection.Connection;
import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.client.gateway.handler.GatewayErrorHandler;
import com.hcq.elion.client.gateway.handler.GatewayOKHandler;
import com.hcq.elion.common.MessageDispatcher;
import com.hcq.elion.network.netty.udp.UDPChannelHandler;
import com.hcq.elion.network.netty.udp.NettyUDPConnector;
import com.hcq.elion.tools.Utils;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.config.CC.elion.net.rcv_buf;
import com.hcq.elion.tools.config.CC.elion.net.snd_buf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import static com.hcq.elion.common.MessageDispatcher.POLICY_LOG;

/**
 */
public final class GatewayUDPConnector extends NettyUDPConnector {

    private UDPChannelHandler channelHandler;
    private MessageDispatcher messageDispatcher;
    private ElionClient elionClient;

    public GatewayUDPConnector(ElionClient elionClient) {
        super(CC.elion.net.gateway_client_port);
        this.elionClient = elionClient;
        this.messageDispatcher = new MessageDispatcher(POLICY_LOG);
    }

    @Override
    public void init() {
        super.init();
        messageDispatcher.register(Command.OK, () -> new GatewayOKHandler(elionClient));
        messageDispatcher.register(Command.ERROR, () -> new GatewayErrorHandler(elionClient));
        channelHandler = new UDPChannelHandler(messageDispatcher);
        channelHandler.setMulticastAddress(Utils.getInetAddress(CC.elion.net.gateway_client_multicast));
        channelHandler.setNetworkInterface(Utils.getLocalNetworkInterface());
    }


    @Override
    public void stop(Listener listener) {
        super.stop(listener);
    }


    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);
        b.option(ChannelOption.IP_MULTICAST_TTL, 255);
        if (snd_buf.gateway_client > 0) b.option(ChannelOption.SO_SNDBUF, snd_buf.gateway_client);
        if (rcv_buf.gateway_client > 0) b.option(ChannelOption.SO_RCVBUF, rcv_buf.gateway_client);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public Connection getConnection() {
        return channelHandler.getConnection();
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }
}
