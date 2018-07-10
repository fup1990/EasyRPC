package com.gome.fup.easy.rpc.remoting;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * 请求处理器
 * Created by fupeng-ds on 2018/7/10.
 */
public interface RequestProcessor {

    RemotingResponse processRequest(ChannelHandlerContext ctx, RemotingRequest request) throws Exception;

}
