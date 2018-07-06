package com.gome.fup.easy.rpc.remoting.test;

import com.gome.fup.easy.rpc.remoting.RemotingServer;
import com.gome.fup.easy.rpc.remoting.server.NettyRemotingServer;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class TestServer {

    public static void main(String[] args) {
        RemotingServer server = new NettyRemotingServer();
        server.start();
    }

}
