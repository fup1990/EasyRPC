package com.gome.fup.easy.rpc.remoting.process;

import com.gome.fup.easy.rpc.remoting.RequestProcessor;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public class ServerProcessor implements RequestProcessor {

    public RemotingResponse processRequest(ChannelHandlerContext ctx, RemotingRequest request) throws Exception {
        request.setBody("hello world!".getBytes());
        ctx.channel().writeAndFlush(request);
        return null;
    }
}
