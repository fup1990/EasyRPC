package com.gome.fup.easy.rpc.remoting.protocol;

import com.gome.fup.easy.rpc.remoting.RemotingCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by fupeng on 2018/7/7.
 */
public class ResponseFuture {

    private boolean sendOk;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private RemotingResponse response;

    private RemotingCallback callback;

    public void await(int timeout) throws InterruptedException {
        countDownLatch.await(timeout, TimeUnit.SECONDS);
    }

    public void countDown() {
        countDownLatch.countDown();
    }

    public boolean isSendOk() {
        return sendOk;
    }

    public void setSendOk(boolean sendOk) {
        this.sendOk = sendOk;
    }

    public RemotingResponse getResponse() {
        return response;
    }

    public void setResponse(RemotingResponse response) {
        this.response = response;
    }

    public RemotingCallback getCallback() {
        return callback;
    }

    public void setCallback(RemotingCallback callback) {
        this.callback = callback;
    }
}
