package com.gome.fup.easy.rpc.nameserver.data;

import com.gome.fup.easy.rpc.common.model.ProducerInfo;
import com.gome.fup.easy.rpc.common.model.RegisterInfo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by fupeng-ds on 2018/7/11.
 */
public class Database {

    private ConcurrentMap<String /*IP*/, List<ProducerInfo> /*服务信息*/> data = new ConcurrentHashMap<String, List<ProducerInfo>>();

    private ConcurrentMap<String /*interfaceName*/, String /*IP*/> interfaces = new ConcurrentHashMap<String, String>();

    //操作日志文件对象
    private CommitLog commitLog;

    //快照文件对象
    private SnapLog snapLog;

    public Database() {
        this.commitLog = new CommitLog();
        this.snapLog = new SnapLog();
    }

    public void register(RegisterInfo registerInfo) {
        String ip = registerInfo.getIp();
        List<ProducerInfo> producers = registerInfo.getProducers();
        data.put(ip, producers);
        for (ProducerInfo info : producers) {
            interfaces.put(info.getInterfaceName(), ip);
        }
    }

}
