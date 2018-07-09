package com.gome.fup.easy.rpc.remoting.client;

import com.gome.fup.easy.rpc.common.thread.EasyThreadFactory;
import com.gome.fup.easy.rpc.common.utils.SocketAddressUtil;
import com.gome.fup.easy.rpc.remoting.RemotingCallback;
import com.gome.fup.easy.rpc.remoting.RemotingClient;
import com.gome.fup.easy.rpc.remoting.handler.DecoderHandler;
import com.gome.fup.easy.rpc.remoting.handler.EncoderHandler;
import com.gome.fup.easy.rpc.remoting.protocol.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static com.gome.fup.easy.rpc.remoting.protocol.MessageType.*;

/**
 * Created by fupeng-ds on 2018/7/6.
 */
public class NettyRemotingClient implements RemotingClient {

    private static final Logger log = LoggerFactory.getLogger(NettyRemotingClient.class);

    private Bootstrap bootstrap;

    private ConcurrentMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    private ConcurrentMap<Long, ResponseFuture> responseFutureMap = new ConcurrentHashMap<Long, ResponseFuture>();

    private ExecutorService callbackService;

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
                                        new IdleStateHandler(0, 0, 120),
                                        new ClientHandler());
                    }
                });
        callbackService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new EasyThreadFactory("AsyncCallbackThread_"));
    }

    /**
     * 建立连接
     * @param address
     * @return
     */
    public Channel connect(String address) {
        ChannelFuture connect = bootstrap.connect(SocketAddressUtil.string2SocketAddress(address));
        if (connect != null) {
            connect.awaitUninterruptibly();
            if (connect.channel() != null && connect.channel().isActive()) {
                log.info("remoting client connect server success, address is {} success", address);
                return connect.channel();
            }
        }
        log.warn("remoting client connect server fail, address is {}", address);
        return null;
    }

    /**
     * 同步发送消息
     * @param address
     * @param request
     * @param timeout
     * @return
     * @throws InterruptedException
     */
    public RemotingResponse sendSync(String address, RemotingRequest request, int timeout) throws InterruptedException {
        final ResponseFuture responseFuture = new ResponseFuture();
        responseFutureMap.put(request.getMsgId(), responseFuture);
        Channel channel = getChannel(address);
        request.setType(SYNC);
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    responseFuture.setSendOk(true);
                } else {
                    responseFuture.setSendOk(false);
                }
            }
        });
        log.info("response future is waiting fo response, msg id is {}", request.getMsgId());
        responseFuture.await(timeout);
        return responseFuture.getResponse();
    }

    /**
     * 异步发送消息
     * @param address
     * @param request
     * @param timeout
     * @param callback
     */
    public void sendAsync(String address, RemotingRequest request, int timeout, final RemotingCallback callback) {
        final ResponseFuture responseFuture = new ResponseFuture();
        responseFuture.setCallback(callback);
        responseFutureMap.put(request.getMsgId(), responseFuture);
        Channel channel = getChannel(address);
        request.setType(ASYNC);
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    responseFuture.setSendOk(true);
                } else {
                    responseFuture.setSendOk(false);
                }
            }
        });
    }

    /**
     * 获取连接
     * @param address
     * @return
     */
    private Channel getChannel(String address) {
        Channel channel = channelMap.get(address);
        if (channel == null) {
            channel = connect(address);
            if (channel != null) {
                channelMap.put(address, channel);
            }
        }
        return channel;
    }

    public class ClientHandler extends SimpleChannelInboundHandler<RemotingMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingMessage msg) throws Exception {
            switch (msg.getType()) {
                case SYNC:
                    processSync(msg);
                    break;
                case ASYNC:
                    processAsync(msg);
                    break;
            }
        }

    }

    /**
     * 处理异步请求
     * @param msg
     */
    private void processAsync(final RemotingMessage msg) {
        final ResponseFuture responseFuture = responseFutureMap.remove(msg.getMsgId());
        if (responseFuture != null) {
            callbackService.submit(new Runnable() {
                public void run() {
                    RemotingResponse response = new RemotingResponse();
                    response.setHeaderCode(msg.getHeaderCode());
                    response.setBody(msg.getBody());
                    RemotingCallback callback = responseFuture.getCallback();
                    callback.call(response);
                }
            });
        }
    }

    /**
     * 处理同步请求
     * @param msg
     */
    private void processSync(RemotingMessage msg) {
        ResponseFuture responseFuture = responseFutureMap.remove(msg.getMsgId());
        if (responseFuture != null) {
            RemotingResponse response = new RemotingResponse();
            response.setHeaderCode(msg.getHeaderCode());
            response.setBody(msg.getBody());
            responseFuture.setResponse(response);
            responseFuture.countDown();
        }
    }
}
