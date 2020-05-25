
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class ClientServerHandler extends ChannelInboundHandlerAdapter {
    private enum SENDING {
        NOTHING,FILESETTINGS,FILE,MESSAGESETTING,MESSAGE,COMMANDSETTINGS,COMMAND;
    }
    private SENDING type = SENDING.NOTHING;
    private int nameLengthFile = 0;
    private String nameFile = "";
    private long sizeFile = 0L;
    private long readFilebyte = 0L;
    private int messageLength = 0;
    private String message = "";
    private int commandLength = 0;
    private String command = "";
    private Path newPathFile;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        while (in.readableBytes()>0) {
            if(type == SENDING.NOTHING){
                switch (in.readByte()) {
                    case (26):
                        type = SENDING.FILESETTINGS;
                        break;
                    case (69):
                        type = SENDING.MESSAGE;
                        ctx.fireChannelRead(msg);
                        break;
                    case (96):
                        type = SENDING.COMMAND;
                        ctx.fireChannelRead(msg);
                        break;
                    default:
                        type = SENDING.NOTHING;
                }
            }else ctx.close();
            if(type == SENDING.FILESETTINGS && in.readableBytes() == 4 && nameLengthFile == 0){
                nameLengthFile = in.readInt();
            }
            if(type == SENDING.FILESETTINGS && in.readableBytes() == nameLengthFile && nameFile == ""){
                byte [] file = in.array();
                 nameFile = new String(file);
//                 Set<PosixFilePermission> posixFilePermissionSet = PosixFilePermissions.fromString("rw-------");
//                 FileAttribute<Set<PosixFilePermission>> fileAttribute = PosixFilePermissions.asFileAttribute(posixFilePermissionSet);
//                 newPathFile = Files.createFile(Paths.get("./Cloud-server/"+ nameFile),fileAttribute);
                Files.write(Paths.get("./Cloud-server/"+ "_"+nameFile),new byte[]{},StandardOpenOption.CREATE);
            }
            if (type == SENDING.FILESETTINGS && nameFile != "" && sizeFile ==0){
                sizeFile = in.readLong();
                type = SENDING.FILE;
            }
            if (type == SENDING.FILE){
                if(in.readableBytes()>0 && readFilebyte>=sizeFile){
                    readFilebyte++;
                    Files.write(newPathFile, new byte[]{in.readByte()},StandardOpenOption.APPEND);
                }
            }
            if (type == SENDING.MESSAGE){

            }
        }
        if(in.readableBytes() == 0)
        in.release();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}