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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetSocketAddress;
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
    private Path path = Paths.get("./Cloud-client");
    private DirectoryStream<Path> files = null;
    private Channel channelClient;

    @FXML
    private TextArea fileOnServer;
    @FXML
    private TextField loadingPathFile;
    @FXML
    private TextArea bootProcess;
    @FXML
    private Button updateFiles;
    @FXML
    private Button loadingFiles;
//    @FXML




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start();
        viewListFiles();
    }

    public void start(){
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(clientGroup)
                    .channel(NioSocketChannel.class)
                    //.remoteAddress( new InetSocketAddress(IP_ADRESS,PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel socketChannel) throws Exception {
//                                     socketChannel.pipeline().addLast(
//                                             new ClientAdapterHandler()
//                                     );
                                     channelClient = socketChannel;
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
    public DirectoryStream<Path> listFiles(){
        try {
            files = Files.newDirectoryStream(path);
        } catch (IOException e) {
            fileOnServer.setText("File not Find");
        }
        return files;
    }
    public void viewListFiles(){
            files = listFiles();
            fileOnServer.clear();
        for (Path file:files) {
            fileOnServer.appendText(file.getFileName().toString());
            fileOnServer.appendText("\n");
        }
    }
    public void infoLoadFile() throws IOException {
        files = listFiles();
            for (Path file:files) {
                if (!Files.isDirectory(file) && (file.getFileName().toString().equals(loadingPathFile.getText()))){
                    bootProcess.appendText(file.getFileName().toString()+ "  \t - " +Files.size(file)+" bytes \t" + " ..........\t "+" Completed \n" );

                }
            }
    }

    public void Dispose() {

    }
}
