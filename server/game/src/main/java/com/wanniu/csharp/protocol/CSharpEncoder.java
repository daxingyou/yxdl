package com.wanniu.csharp.protocol;

import com.wanniu.core.tcp.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class CSharpEncoder
        extends MessageToByteEncoder<Message> {
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.getContent());
    }
}


