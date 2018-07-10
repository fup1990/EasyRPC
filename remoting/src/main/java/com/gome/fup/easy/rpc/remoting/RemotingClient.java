package com.gome.fup.easy.rpc.remoting;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public interface RemotingClient extends RemotingService{

    //同步发送消息
    RemotingResponse sendSync(String address, RemotingRequest request, int timeout) throws InterruptedException;

    //异步发送消息
    void sendAsync(String address, RemotingRequest request, int timeout, RemotingCallback callback);

}
