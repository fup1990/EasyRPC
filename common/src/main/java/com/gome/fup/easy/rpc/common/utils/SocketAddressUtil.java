package com.gome.fup.easy.rpc.common.utils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class SocketAddressUtil {

    public static SocketAddress string2SocketAddress(final String address) {
        String[] s = address.split(":");
        return new InetSocketAddress(s[0], Integer.parseInt(s[1]));
    }

}
