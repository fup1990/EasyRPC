package com.gome.fup.easy.rpc.remoting.protocol;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class RemotingRequest {

    private int headerCode;

    private byte[] body;

    public RemotingRequest() {
    }

    public RemotingRequest(int headerCode, byte[] body) {
        this.headerCode = headerCode;
        this.body = body;
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
}
