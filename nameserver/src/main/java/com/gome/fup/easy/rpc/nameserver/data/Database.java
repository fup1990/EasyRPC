package com.gome.fup.easy.rpc.nameserver.data;

/**
 * Created by fupeng-ds on 2018/7/11.
 */
public class Database {

    private NodeTree dataTree;

    private CommitLog commitLog;

    public Database() {
        this.dataTree = new NodeTree();
        this.commitLog = new CommitLog();
    }

    public void shutdown() {
        commitLog.shutdown();
    }

}
