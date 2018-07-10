package com.gome.fup.easy.rpc.remoting.config;

/**
 * Created by fupeng-ds on 2018/6/21.
 */
public class RemotingConfig {

    public static Integer THREAD_NUM;

    static {
        if (THREAD_NUM == null) {
            THREAD_NUM = Runtime.getRuntime().availableProcessors();
        }
    }

}
