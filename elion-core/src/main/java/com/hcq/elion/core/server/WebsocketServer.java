
package com.hcq.elion.core.server;

import com.hcq.elion.api.connection.ConnectionManager;
import com.hcq.elion.api.protocol.Command;
import com.hcq.elion.api.service.Listener;
import com.hcq.elion.api.spi.handler.PushHandlerFactory;
import com.hcq.elion.common.MessageDispatcher;
import com.hcq.elion.core.ElionServer;
import com.hcq.elion.core.handler.AckHandler;
import com.hcq.elion.core.handler.BindUserHandler;
import com.hcq.elion.core.handler.HandshakeHandler;
import com.hcq.elion.network.netty.server.NettyTCPServer;
import com.hcq.elion.tools.config.CC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;


public final class WebsocketServer extends NettyTCPServer {

    private final ChannelHandler channelHandler;

    private final MessageDispatcher messageDispatcher;

    private final ConnectionManager connectionManager;

    private final ElionServer elionServer;

    public WebsocketServer(ElionServer elionServer) {
        super(CC.elion.net.ws_server_port);
        this.elionServer = elionServer;
        this.messageDispatcher = new MessageDispatcher();
        this.connectionManager = new ServerConnectionManager(false);
        this.channelHandler = new WebSocketChannelHandler(connectionManager, messageDispatcher);
    }

    @Override
    public void init() {
        super.init();
        connectionManager.init();
        messageDispatcher.register(Command.HANDSHAKE, () -> new HandshakeHandler(elionServer));
        messageDispatcher.register(Command.BIND, () -> new BindUserHandler(elionServer));
        messageDispatcher.register(Command.UNBIND, () -> new BindUserHandler(elionServer));
        messageDispatcher.register(Command.PUSH, PushHandlerFactory::create);
        messageDispatcher.register(Command.ACK, () -> new AckHandler(elionServer));
    }

    @Override
    public void stop(Listener listener) {
        super.stop(listener);
        connectionManager.destroy();
    }

    @Override
    public EventLoopGroup getBossGroup() {
        return elionServer.getConnectionServer().getBossGroup();
    }

    @Override
    public EventLoopGroup getWorkerGroup() {
        return elionServer.getConnectionServer().getWorkerGroup();
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(CC.elion.net.ws_path, null, true));
        pipeline.addLast(new WebSocketIndexPageHandler());
        pipeline.addLast(getChannelHandler());
    }

    @Override
    protected void initOptions(ServerBootstrap b) {
        super.initOptions(b);
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.childOption(ChannelOption.SO_SNDBUF, 32 * 1024);
        b.childOption(ChannelOption.SO_RCVBUF, 32 * 1024);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }
}
