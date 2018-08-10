package com.gome.fup.easy.rpc.nameserver.test;

import com.gome.fup.easy.rpc.nameserver.data.CommitLog;

/**
 * Created by fupeng on 2018/7/15.
 */
public class CommitLogTest {

    public static void main(String[] args) {
        final CommitLog commitLog = new CommitLog();
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                commitLog.write("def456".getBytes());
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                commitLog.write("123abc".getBytes());
            }
        });
        thread1.start();
        thread2.start();
//        byte[] bytes = snapLog.readMessage(10, 20);
//        System.out.println(new String(bytes));
//        byte[] bytes1 = snapLog.readMessage(5, 25);
//        System.out.println(new String(bytes1));
    }

}
