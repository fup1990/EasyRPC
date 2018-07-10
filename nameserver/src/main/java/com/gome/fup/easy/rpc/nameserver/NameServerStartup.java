package com.gome.fup.easy.rpc.nameserver;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public class NameServerStartup {

    public static void main(String[] args) {
        NameServer server = new NameServer();
        server.start();
    }

}
