package SEND;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

import java.io.IOException;

public class SendCommands {
        public static void sendCommand (String command, Channel channel) throws IOException {
            byte SIGNAL_COMMAND_BYTE = 96;
            int commandLength = command.length();

            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(1+4+commandLength) ;
            byteBuf.writeByte(SIGNAL_COMMAND_BYTE);
            byteBuf.writeInt(commandLength);
            byteBuf.writeBytes(command.getBytes());
            channel.writeAndFlush(byteBuf);
        }
}

