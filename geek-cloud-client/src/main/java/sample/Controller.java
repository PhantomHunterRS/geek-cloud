package sample;

import ru.phantomhunter.send.SendingAFile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Path path = Paths.get("./Cloud-client");
    public DirectoryStream<Path> files = null;
    public NetworkConnect nc;


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
        nc = new NetworkConnect();
        new Thread(() -> nc.start()).start();
        //viewListFiles();
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
                    bootProcess.appendText(file.getFileName().toString()+ "  \t - " +Files.size(file)+" bytes \t" + file.toFile().toString().length() +" \t "+" Completed \n" );
                    SendingAFile.sendFile(file,nc.getChannelClient(),future ->{
                        if (!future.isSuccess()){
                            System.out.println("no");
                            future.cause().printStackTrace();
                        }
                        if(future.isSuccess()) System.out.println("File send");
                    });
                }
            }
    }

}
