package com.gome.fup.easy.rpc.remoting.protocol;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class RemotingRequest extends RemotingMessage{

    public RemotingRequest() {
    }

    public RemotingRequest(long msgId, int type, int headerCode, byte[] body) {
        super(msgId, type, headerCode, body);
    }
}
