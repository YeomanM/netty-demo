package com.wc.socket;

import com.wc.websocket.HttpRequestHandler;
import com.wc.websocket.TextWebSocketFrameHandler;
import com.wc.websocket.WebsocketChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

//public class WebSocketChat {
//
//    private int port;
//
//    public WebSocketChat(int port) {
//        this.port = port;
//    }
//
//    public void run() {
//        NioEventLoopGroup boss = new NioEventLoopGroup(1);
//        NioEventLoopGroup worker = new NioEventLoopGroup();
//
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(boss, worker)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel channel) throws Exception {
//                            channel.pipeline().addLast(new HttpServerCodec())
//                                .addLast(new HttpServerCodec())
//                                .addLast(new HttpObjectAggregator(64*1024))
//                                .addLast(new ChunkedWriteHandler())
//                                .addLast(new HttpRequestHandler("/ws"))
//                                .addLast(new WebSocketServerProtocolHandler("/ws"))
//                                .addLast(new TextWebSocketFrameHandler());
//                        }
//                    })
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
//
//            ChannelFuture f = b.bind(this.port).sync();
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
//        }
//    }
//
//    public static void main(String[] args) {
//        new WebSocketChat(8080).run();
//    }
//
//}

public class WebSocketChat {

    private int port;

    public WebSocketChat(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChatServerInitializer())  //(4)
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            System.out.println("WebsocketChatServer 启动了");

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync(); // (7)

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            System.out.println("WebsocketChatServer 关闭了");
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new WebSocketChat(port).run();

    }
}
