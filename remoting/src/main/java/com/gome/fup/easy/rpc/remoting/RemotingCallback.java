package com.gome.fup.easy.rpc.remoting;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public interface RemotingCallback {

    void call(RemotingResponse response);

}
