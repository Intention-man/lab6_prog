import functional_classes.ClientReader;
import functional_classes.ClientManager;
import functional_classes.Writer;

import java.util.Objects;


public class ClientMain {
    public static void main(String[] args) {
        String executedCommand;
        try {
            // initialization

            ClientReader reader = new ClientReader();
            Writer writer = new Writer();
            ClientManager clientManager = new ClientManager(reader, writer);
            reader.setClientManager(clientManager);

            // execution

            writer.suggestNewAction();
            executedCommand = reader.readNextLine().trim();
            while (!Objects.equals(executedCommand, "exit")) {
                clientManager.startNewAction(executedCommand);
                writer.suggestNewAction();
                executedCommand = reader.readNextLine().trim();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}