package ru.phantomhunter.send;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.IOException;

public class SendMessages {
        public static void sendMessage (String message, Channel channel) throws IOException {
            byte SIGNAL_MESSAGE_BYTE = 69;
            int messageLength = message.length();

            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(1+4+messageLength) ;
            byteBuf.writeByte(SIGNAL_MESSAGE_BYTE);
            byteBuf.writeInt(messageLength);
            byteBuf.writeBytes(message.getBytes());
            channel.writeAndFlush(byteBuf);
        }
    }

