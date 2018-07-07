package com.gome.fup.easy.rpc.remoting.handler;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * Created by fupeng-ds on 2018/6/20.
 */
public class DecoderHandler extends LengthFieldBasedFrameDecoder {

    private static final int maxFrameLength = 16777216;

    private static final int lengthFieldOffset = 0;

    private static final int lengthFieldLength = 4;

    public DecoderHandler() {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuffer buffer = in.nioBuffer();
        //消息头
        int code = buffer.getInt();
        //消息大小
        int size = buffer.getInt();
        //消息ID
        long msgId = buffer.getLong();
        //消息内容
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return new RemotingMessage(msgId, code, bytes);
    }
}
