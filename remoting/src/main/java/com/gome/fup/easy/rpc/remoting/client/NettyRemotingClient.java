package com.gome.fup.easy.rpc.remoting.client;

import com.gome.fup.easy.rpc.common.utils.SocketAddressUtil;
import com.gome.fup.easy.rpc.remoting.RemotingCallback;
import com.gome.fup.easy.rpc.remoting.RemotingClient;
import com.gome.fup.easy.rpc.remoting.handler.ClientHandler;
import com.gome.fup.easy.rpc.remoting.handler.DecoderHandler;
import com.gome.fup.easy.rpc.remoting.handler.EncoderHandler;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class NettyRemotingClient implements RemotingClient {

    private static final Logger log = LoggerFactory.getLogger(NettyRemotingClient.class);

    private Bootstrap bootstrap;

    private ConcurrentMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    public NettyRemotingClient() {
        this.bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        final DefaultEventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(8);
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(eventExecutorGroup,
                                        new EncoderHandler(),
                                        new DecoderHandler(),
                                        new IdleStateHandler(0, 0, 120));
                    }
                });
    }

    public Channel connect(String address) {
        ChannelFuture connect = bootstrap.connect(SocketAddressUtil.string2SocketAddress(address));
        log.info("remoting client connect server, address is {}", address);
        return connect.channel();
    }

    public RemotingResponse sendSync(String address, RemotingRequest request, int timeout) throws InterruptedException {
        final RemotingResponse response = new RemotingResponse();
        Channel channel = getChannel(address);
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    response.setSendOk(true);
                } else {
                    response.setSendOk(false);
                }
                response.countDown();
            }
        });
        response.await(timeout);
        return response;
    }

    public void sendAsync(String address, RemotingRequest request, int timeout, final RemotingCallback callback) {
        Channel channel = getChannel(address);
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    callback.call();
                }
            }
        });
    }

    private Channel getChannel(String address) {
        Channel channel = channelMap.get(address);
        if (channel == null) {
            channel = connect(address);
            channelMap.put(address, channel);
        }
        return channel;
    }
}
