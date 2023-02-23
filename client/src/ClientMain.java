import functional_classes.Receiver;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;


public class ClientMain {
    public static void main(String[] args) {
        try {
            Receiver.suggestNewAction();
            while (!Objects.equals(Receiver.getExecutedCommand(), "exit")) {
                Receiver.startNewAction(Receiver.getExecutedCommand());
                Receiver.suggestNewAction();
            }
        } catch (Exception e) {
            System.out.println("e.getMessage()");
        }
    }
}