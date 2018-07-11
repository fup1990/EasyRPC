package com.gome.fup.easy.rpc.nameserver.processor;

import com.gome.fup.easy.rpc.common.model.RegisterInfo;
import com.gome.fup.easy.rpc.nameserver.NameServer;
import com.gome.fup.easy.rpc.nameserver.data.Database;
import com.gome.fup.easy.rpc.remoting.RequestProcessor;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import com.gome.fup.easy.rpc.remoting.server.AbstractRequestProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public class RegisterProviderRequestProcessor extends AbstractRequestProcessor implements RequestProcessor {

    private NameServer nameServer;

    private Database database;

    public RegisterProviderRequestProcessor(NameServer nameServer) {
        this.nameServer = nameServer;
        this.database = this.nameServer.getDatabase();
    }

    public RemotingResponse processRequest(ChannelHandlerContext ctx, RemotingRequest request) throws Exception {
        RegisterInfo registerInfo = super.decodeRequestBody(request.getBody(), RegisterInfo.class);
        database.register(registerInfo);
        return null;
    }
}
