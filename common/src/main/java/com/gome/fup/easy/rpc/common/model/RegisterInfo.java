package com.gome.fup.easy.rpc.common.model;

import java.util.List;

/**
 * Created by fupeng-ds on 2018/7/11.
 */
public class RegisterInfo {

    private String ip;

    private List<ProducerInfo> producers;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<ProducerInfo> getProducers() {
        return producers;
    }

    public void setProducers(List<ProducerInfo> producers) {
        this.producers = producers;
    }
}
