package com.gome.fup.easy.rpc.nameserver.processor;

import com.gome.fup.easy.rpc.remoting.RequestProcessor;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public class RegisterProviderRequestProcessor implements RequestProcessor {

    public RemotingResponse processRequest(ChannelHandlerContext ctx, RemotingRequest request) throws Exception {
        return null;
    }
}
