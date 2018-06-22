package com.gome.fup.easy.rpc.remoting.server;

import com.gome.fup.easy.rpc.remoting.RemotingService;
import com.gome.fup.easy.rpc.remoting.handler.DecoderHandler;
import com.gome.fup.easy.rpc.remoting.handler.EncoderHandler;
import com.gome.fup.easy.rpc.remoting.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * netty 服务端
 */
public class RemotingServer implements RemotingService {
    private static final Logger log = LoggerFactory.getLogger(RemotingServer.class);
    private final ServerBootstrap bootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private DefaultEventExecutorGroup eventExecutorGroup;
    private int port;

    public RemotingServer() {
        this.bootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(8);
        this.eventExecutorGroup = new DefaultEventExecutorGroup(8);
    }

    /**
     * 启动netty服务
     */
    public void start() {
        this.bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .localAddress(new InetSocketAddress(10101))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(eventExecutorGroup,
                                        new EncoderHandler(),
                                        new DecoderHandler(),
                                        new IdleStateHandler(0, 0, 120),
                                        new ServerHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = this.bootstrap.bind().sync();
            InetSocketAddress address = (InetSocketAddress) channelFuture.channel().localAddress();
            this.port = address.getPort();
        } catch (InterruptedException e) {
            log.error("this.bootstrap.bind().sync() InterruptedException", e);
            throw new RuntimeException("this.bootstrap.bind().sync() InterruptedException", e);
        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
        if (this.eventExecutorGroup != null) {
            this.eventExecutorGroup.shutdownGracefully();
        }
    }

    public int getPort() {
        return port;
    }
}