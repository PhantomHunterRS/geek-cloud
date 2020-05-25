import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SendingAFile{
    public static void sendFile (Path path, Channel channel, ChannelFutureListener channelFutureListener) throws IOException {
        byte SIGNAL_FILE_BYTE = 26;
        byte[] nameFile = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        int nameFileLength = path.toFile().toString().length();
        long sizeFile = Files.size(path) ;

        FileRegion fileRegion = new DefaultFileRegion(path.toFile(),0, sizeFile);

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(1 + 4 +nameFileLength + 8) ;
        byteBuf.writeByte(SIGNAL_FILE_BYTE);
        byteBuf.writeInt(nameFileLength);
        byteBuf.writeBytes(nameFile);
        byteBuf.writeLong(sizeFile);
        channel.writeAndFlush(byteBuf);

        ChannelFuture channelFuture = channel.writeAndFlush(fileRegion);
        if(channelFutureListener != null){
            channelFuture.addListener(channelFutureListener);
        }

    }

}
