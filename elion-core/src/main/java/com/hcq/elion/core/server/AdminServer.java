
package com.hcq.elion.core.server;

import com.hcq.elion.core.ElionServer;
import com.hcq.elion.core.handler.AdminHandler;
import com.hcq.elion.network.netty.server.NettyTCPServer;
import com.hcq.elion.tools.config.CC;
import com.hcq.elion.tools.thread.ThreadNames;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public final class AdminServer extends NettyTCPServer {

    private AdminHandler adminHandler;

    private ElionServer elionServer;

    public AdminServer(ElionServer elionServer) {
        super(CC.elion.net.admin_server_port);
        this.elionServer = elionServer;
    }

    @Override
    public void init() {
        super.init();
        this.adminHandler = new AdminHandler(elionServer);
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        super.initPipeline(pipeline);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return adminHandler;
    }

    @Override
    protected ChannelHandler getDecoder() {
        return new StringDecoder();
    }

    @Override
    protected ChannelHandler getEncoder() {
        return new StringEncoder();
    }

    @Override
    protected int getWorkThreadNum() {
        return 1;
    }

    @Override
    protected String getBossThreadName() {
        return ThreadNames.T_ADMIN_BOSS;
    }

    @Override
    protected String getWorkThreadName() {
        return ThreadNames.T_ADMIN_WORKER;
    }
}
