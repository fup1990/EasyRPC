package com.gome.fup.easy.rpc.remoting.test;

import com.gome.fup.easy.rpc.remoting.RemotingClient;
import com.gome.fup.easy.rpc.remoting.client.NettyRemotingClient;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class TestClient {

    public static void main(String[] args) throws InterruptedException {
        RemotingRequest request = new RemotingRequest();
        request.setMsgId(System.currentTimeMillis());
        request.setHeaderCode(8);
        request.setBody("测试".getBytes());
        RemotingClient client = new NettyRemotingClient();
        RemotingResponse response = client.sendSync("localhost:10101", request, 10);
        System.out.println(response.getHeaderCode());
    }

}
