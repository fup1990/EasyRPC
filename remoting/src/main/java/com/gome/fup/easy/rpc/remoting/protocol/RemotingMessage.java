package com.gome.fup.easy.rpc.remoting.protocol;

import java.io.Serializable;

/**
 * Created by fupeng on 2018/7/7.
 */
public class RemotingMessage implements Serializable{

    private long msgId;

    private int type;

    private int headerCode;

    private byte[] body;

    public RemotingMessage() {
    }

    public RemotingMessage(long msgId, int type, int headerCode, byte[] body) {
        this.msgId = msgId;
        this.headerCode = headerCode;
        this.body = body;
        this.type = type;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getHeaderCode() {
        return headerCode;
    }

    public void setHeaderCode(int headerCode) {
        this.headerCode = headerCode;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int size() {
        return this.body.length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
