package com.gome.fup.easy.rpc.nameserver;

import com.gome.fup.easy.rpc.nameserver.data.Database;
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

    private Database database;

    public NameServer() {
        this.remotingServer = new NettyRemotingServer();
        this.database = new Database();
        registerProcessor();
    }

    public void start() {
        this.remotingServer.start();
    }

    private void registerProcessor() {
        this.remotingServer.registerProcessor(RequestHeaderCode.REGISTER_PROVIDER_CODE, new RegisterProviderRequestProcessor(this));
        this.remotingServer.registerProcessor(RequestHeaderCode.PULL_PROVIDER_CODE, new PullProviderRequestProcessor());
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
