package com.gome.fup.easy.rpc.nameserver.test;

import com.gome.fup.easy.rpc.nameserver.data.SnapLog;

/**
 * Created by fupeng on 2018/7/15.
 */
public class SnapLogTest {

    public static void main(String[] args) {
        SnapLog snapLog = new SnapLog();
        boolean flag = snapLog.appendMessage("hello world".getBytes());
        System.out.println(flag);
    }

}
