package sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Socket socket;
    private String IP_ADRESS = "127.0.0.1";
    private int PORT = 8989;

    @FXML
    private TextArea fileOnServer;
//    @FXML
//    @FXML
//    @FXML
//    @FXML
//    @FXML




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start();
        listFiles();
    }

    public void start(){
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel socketChannel) throws Exception {
//                                     socketChannel.pipeline().addLast(
//                                             new ClientAdapterHandler()
//                                     );
                                     System.out.println("Client connected");
                                 }
                    });
                        Channel channel = bootstrap.connect(IP_ADRESS, PORT).sync().channel();
                            channel.write("Hi\n");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clientGroup.shutdownGracefully();
        }
    }
    public void listFiles(){
        fileOnServer.clear();
        Path path = Paths.get("./Cloud-server");
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(path);
            for (Path file:files) {
                fileOnServer.appendText(file.getFileName().toString());
                fileOnServer.appendText("\n");
            }
        } catch (IOException e) {
            fileOnServer.setText("File not Find");
        }

    }
    public void Dispose() {

    }
}
