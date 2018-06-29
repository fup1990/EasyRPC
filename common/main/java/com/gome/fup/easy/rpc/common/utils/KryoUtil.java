package com.gome.fup.easy.rpc.common.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

/**
 * Kryo序列化工具类
 *
 * @author fupeng-ds
 */
public class KryoUtil {

	private static Kryo kryo = new Kryo();

	static {
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
	}
	
	public static <T> T byteToObj(byte[] buffer, Class<T> type) {
		Input input = null;
		try {
			input = new Input(buffer);
			return kryo.readObject(input, type);
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
	
	public static <T> byte[] objToByte(T t) {
		Output output = null;
		try {
			output = new Output(new ByteArrayOutputStream());
			kryo.writeObject(output, t);
			return output.toBytes();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
}
