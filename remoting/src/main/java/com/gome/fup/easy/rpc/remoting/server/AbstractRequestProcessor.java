package com.gome.fup.easy.rpc.remoting.server;

import com.gome.fup.easy.rpc.common.serializable.KryoUtil;
import com.gome.fup.easy.rpc.remoting.RequestProcessor;

/**
 * Created by fupeng-ds on 2018/7/11.
 */
public abstract class AbstractRequestProcessor implements RequestProcessor {

    protected <T> T decodeRequestBody(byte[] bytes, Class<T> t) {
        return KryoUtil.byteToObj(bytes, t);
    }

}
