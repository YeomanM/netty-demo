package com.wc.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        for (Channel c : group) {
            c.writeAndFlush("[SERVER] - " + channel.remoteAddress() + " 加入\n");
        }
        group.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel out = ctx.channel();
        for (Channel channel : group) {
            channel.writeAndFlush("[SERVER] - " + out.remoteAddress() + " 离开\n");
        }
        group.remove(out);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String o) throws Exception {
        Channel say = ctx.channel();
        for (Channel channel : group) {
            if (say == channel) {
                channel.writeAndFlush("[YOU] - " + o + "\n");
            } else {
                channel.writeAndFlush("[" + say.remoteAddress() + "] - " + o + " \n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("SimpleChatClient:"+channel.remoteAddress()+"在线");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("SimpleChatClient:"+channel.remoteAddress()+"掉线");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("SimpleChatClient:"+channel.remoteAddress()+"异常");
        cause.printStackTrace();
        ctx.close();
    }
}
