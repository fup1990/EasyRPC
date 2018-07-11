package com.gome.fup.easy.rpc.common.model;

import java.util.List;

/**
 * Created by fupeng-ds on 2018/7/11.
 */
public class ProducerInfo {

    private String interfaceName;

    private List<String> methods;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
