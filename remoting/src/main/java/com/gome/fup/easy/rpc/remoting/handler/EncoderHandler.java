package com.gome.fup.easy.rpc.remoting.handler;

import com.gome.fup.easy.rpc.remoting.protocol.RemotingMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by fupeng-ds on 2018/6/20.
 */
public class EncoderHandler extends MessageToByteEncoder<RemotingMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RemotingMessage request, ByteBuf buf) throws Exception {
        //消息类型
        buf.writeInt(request.getType());
        //请求头类型
        buf.writeInt(request.getHeaderCode());
        //消息长度
        buf.writeInt(request.size());
        //消息ID
        buf.writeLong(request.getMsgId());
        //消息内容
        buf.writeBytes(request.getBody());
    }

}
