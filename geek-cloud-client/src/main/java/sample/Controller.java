package sample;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.fxml.Initializable;

import javax.rmi.PortableRemoteObject;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Socket socket;
    private String IP_ADRESS = "127.0.0.1";
    private int PORT = 8989;
    private static ObjectEncoderOutputStream loading;
    private static ObjectDecoderInputStream download;

    public void initialize(URL location, ResourceBundle resources) {


    }
    public void connect(){
        try {
            socket = new Socket(IP_ADRESS, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Dispose(){

    }
}
