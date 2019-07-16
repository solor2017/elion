
package com.hcq.elion.client.gateway;

import com.hcq.elion.api.connection.ConnectionManager;
import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.client.ElionClient;
import com.hcq.elion.client.gateway.handler.GatewayClientChannelHandler;
import com.hcq.elion.client.gateway.handler.GatewayErrorHandler;
import com.hcq.elion.client.gateway.handler.GatewayOKHandler;
import com.hcq.elion.common.MessageDispatcher;
import com.hcq.elion.network.netty.client.NettyTCPClient;
import com.hcq.elion.network.netty.connection.NettyConnectionManager;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.config.CC.elion.net.rcv_buf;
import com.hcq.elion.tools.config.CC.elion.net.snd_buf;
import com.hcq.elion.tools.thread.NamedPoolThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.sctp.nio.NioSctpChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.hcq.elion.tools.config.CC.elion.net.traffic_shaping.gateway_client.*;
import static com.hcq.elion.tools.thread.ThreadNames.T_TRAFFIC_SHAPING;

/**
 */
public class GatewayClient extends NettyTCPClient {
    private final GatewayClientChannelHandler handler;
    private GlobalChannelTrafficShapingHandler trafficShapingHandler;
    private ScheduledExecutorService trafficShapingExecutor;
    private final ConnectionManager connectionManager;
    private final MessageDispatcher messageDispatcher;

    public GatewayClient(ElionClient elionClient) {
        messageDispatcher = new MessageDispatcher();
        messageDispatcher.register(Command.OK, () -> new GatewayOKHandler(elionClient));
        messageDispatcher.register(Command.ERROR, () -> new GatewayErrorHandler(elionClient));
        connectionManager = new NettyConnectionManager();
        handler = new GatewayClientChannelHandler(connectionManager, messageDispatcher);
        if (enabled) {
            trafficShapingExecutor = Executors.newSingleThreadScheduledExecutor(new NamedPoolThreadFactory(T_TRAFFIC_SHAPING));
            trafficShapingHandler = new GlobalChannelTrafficShapingHandler(
                    trafficShapingExecutor,
                    write_global_limit, read_global_limit,
                    write_channel_limit, read_channel_limit,
                    check_interval);
        }
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        super.initPipeline(pipeline);
        if (trafficShapingHandler != null) {
            pipeline.addFirst(trafficShapingHandler);
        }
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        if (trafficShapingHandler != null) {
            trafficShapingHandler.release();
            trafficShapingExecutor.shutdown();
        }
        super.doStop(listener);
    }

    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        if (snd_buf.gateway_client > 0) b.option(ChannelOption.SO_SNDBUF, snd_buf.gateway_client);
        if (rcv_buf.gateway_client > 0) b.option(ChannelOption.SO_RCVBUF, rcv_buf.gateway_client);
    }

    @Override
    public ChannelFactory<? extends Channel> getChannelFactory() {
        if (CC.elion.net.tcpGateway()) return super.getChannelFactory();
        if (CC.elion.net.udtGateway()) return NioUdtProvider.BYTE_CONNECTOR;
        if (CC.elion.net.sctpGateway()) return NioSctpChannel::new;
        return super.getChannelFactory();
    }

    @Override
    public SelectorProvider getSelectorProvider() {
        if (CC.elion.net.tcpGateway()) return super.getSelectorProvider();
        if (CC.elion.net.udtGateway()) return NioUdtProvider.BYTE_PROVIDER;
        if (CC.elion.net.sctpGateway()) return super.getSelectorProvider();
        return super.getSelectorProvider();
    }

    @Override
    protected int getWorkThreadNum() {
        return CC.elion.thread.pool.gateway_client_work;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }
}
