package com.gome.fup.easy.rpc.common.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class KryoUtil {

    private static Kryo kryo = new Kryo();

    static {
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
    }

    public static <T> T byteToObj(byte[] buffer, Class<T> type) {
        Input input = new Input(buffer);
        return kryo.readObject(input, type);
    }

    public static <T> byte[] objToByte(T t) {
        Output output = new Output(new ByteArrayOutputStream());
        kryo.writeObject(output, t);
        return output.toBytes();
    }

}
