package com.gome.fup.easy.rpc.nameserver.processor;

import com.gome.fup.easy.rpc.remoting.RequestProcessor;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.channel.ChannelHandlerContext;

public class HaRequestProcessor implements RequestProcessor {
    public RemotingResponse processRequest(ChannelHandlerContext ctx, RemotingRequest request) throws Exception {
        return null;
    }
}
