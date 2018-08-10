package com.gome.fup.easy.rpc.nameserver;

import com.gome.fup.easy.rpc.nameserver.data.Database;
import com.gome.fup.easy.rpc.nameserver.processor.HaRequestProcessor;
import com.gome.fup.easy.rpc.nameserver.processor.HandlerRequestProcessor;
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

    public void shutdown() {
        this.remotingServer.shutdown();
        this.database.shutdown();
    }

    private void registerProcessor() {
        this.remotingServer.registerProcessor(RequestHeaderCode.NAME_SERVER_HANDLER_CODE, new HandlerRequestProcessor());
        this.remotingServer.registerProcessor(RequestHeaderCode.NAME_SERVER_HA_CODE, new HaRequestProcessor());
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
