package com.gome.fup.easy.rpc.nameserver.test;

import com.gome.fup.easy.rpc.nameserver.data.SnapLog;

/**
 * Created by fupeng on 2018/7/15.
 */
public class SnapLogTest {

    public static void main(String[] args) {
        SnapLog snapLog = new SnapLog();
        snapLog.appendMessage("def456".getBytes());
        snapLog.appendMessage("123abc".getBytes());
//        byte[] bytes = snapLog.readMessage(10, 20);
//        System.out.println(new String(bytes));
//        byte[] bytes1 = snapLog.readMessage(5, 25);
//        System.out.println(new String(bytes1));
    }

}
