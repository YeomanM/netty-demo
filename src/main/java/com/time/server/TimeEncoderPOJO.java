package com.time.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeEncoderPOJO extends MessageToByteEncoder<Time> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Time time, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt((int) time.getValue());
    }
}
