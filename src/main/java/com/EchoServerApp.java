package com;

import com.wc.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/3/9
 * @desc
 */
public class EchoServerApp {

    private int port;

    public EchoServerApp() {
        this.port = 9527;
    }

    public EchoServerApp(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        NioEventLoopGroup bossLG = new NioEventLoopGroup(1);
        NioEventLoopGroup workerLG = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossLG, workerLG)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossLG.shutdownGracefully();
            workerLG.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        EchoServerApp app = new EchoServerApp();
        app.run();
    }
}
