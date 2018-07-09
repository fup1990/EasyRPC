package com.gome.fup.easy.rpc.remoting.handler;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

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
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        if (buf.isReadable()) {
            //消息头
            int code = buf.readInt();
            //消息大小
            int size = buf.readInt();
            //消息ID
            long msgId = buf.readLong();
            //消息内容
            byte[] bytes = new byte[size];
            buf.readBytes(bytes);
            return new RemotingMessage(msgId, code, bytes);
        }
        return super.decode(ctx, buf);
    }
}
