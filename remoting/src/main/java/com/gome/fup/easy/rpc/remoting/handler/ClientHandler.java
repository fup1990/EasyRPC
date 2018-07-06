package com.gome.fup.easy.rpc.remoting.handler;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class ClientHandler extends SimpleChannelInboundHandler<RemotingRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingRequest msg) throws Exception {

    }

}
