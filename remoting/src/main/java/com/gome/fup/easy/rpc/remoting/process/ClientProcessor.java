package com.gome.fup.easy.rpc.remoting.process;

import com.gome.fup.easy.rpc.remoting.RequestProcessor;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public class ClientProcessor implements RequestProcessor {

    public RemotingResponse processRequest(ChannelHandlerContext ctx, RemotingRequest request) throws Exception {
        RemotingResponse response = new RemotingResponse();
        response.setBody(request.getBody());
        response.setHeaderCode(request.getHeaderCode());
        response.setType(request.getType());
        response.setMsgId(request.getMsgId());
        return response;
    }

}
