package com.wc.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel in = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + in.remoteAddress() + " 加入了聊天室");
        }
        channels.add(in);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel out = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + out.remoteAddress() + " 离开了聊天室");
        }
        channels.remove(out);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        Channel say = ctx.channel();

        for (Channel channel : channels) {
            if (say == channel) {
                say.writeAndFlush(new TextWebSocketFrame("[YOU]" + frame.text()));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("[" + say.remoteAddress() + "]" + frame.text()));
            }
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel out = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + out.remoteAddress() + " 上线了");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel out = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + out.remoteAddress() + " 掉线了");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        System.out.println(ctx.channel().remoteAddress() + " 异常了");
    }
}
