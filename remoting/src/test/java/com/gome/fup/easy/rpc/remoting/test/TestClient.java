package com.gome.fup.easy.rpc.remoting.test;

import com.gome.fup.easy.rpc.common.id.Snowflake;
import com.gome.fup.easy.rpc.remoting.RemotingCallback;
import com.gome.fup.easy.rpc.remoting.RemotingClient;
import com.gome.fup.easy.rpc.remoting.client.NettyRemotingClient;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class TestClient {

    public static void main(String[] args) throws InterruptedException {
        Snowflake snowflake = new Snowflake();
        RemotingRequest request = new RemotingRequest();
        request.setMsgId(snowflake.nextId());
        request.setHeaderCode(8);
        request.setBody("测试".getBytes());
        RemotingClient client = new NettyRemotingClient();
        client.sendAsync("localhost:10101", request, 10, new RemotingCallback() {
            public void call(RemotingResponse response) {
                System.out.println(response.getHeaderCode() + new String(response.getBody()));
            }
        });
    }

}
