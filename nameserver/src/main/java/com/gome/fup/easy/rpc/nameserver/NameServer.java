package com.gome.fup.easy.rpc.nameserver;

import com.gome.fup.easy.rpc.nameserver.processor.PullProviderRequestProcessor;
import com.gome.fup.easy.rpc.nameserver.processor.RegisterProviderRequestProcessor;
import com.gome.fup.easy.rpc.remoting.RemotingServer;
import com.gome.fup.easy.rpc.remoting.config.RequestHeaderCode;
import com.gome.fup.easy.rpc.remoting.server.NettyRemotingServer;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public class NameServer {

    private RemotingServer remotingServer;

    public NameServer() {
        this.remotingServer = new NettyRemotingServer();
        registerProcessor();
    }

    public void start() {
        this.remotingServer.start();
    }

    private void registerProcessor() {
        this.remotingServer.registerProcessor(RequestHeaderCode.REGISTER_PROVIDER_CODE, new RegisterProviderRequestProcessor());
        this.remotingServer.registerProcessor(RequestHeaderCode.REGISTER_PROVIDER_CODE, new PullProviderRequestProcessor());
    }

}
