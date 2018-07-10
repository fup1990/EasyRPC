package com.gome.fup.easy.rpc.remoting;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public interface RemotingService {

    void registerProcessor(int requestCode, RequestProcessor processor);

}
