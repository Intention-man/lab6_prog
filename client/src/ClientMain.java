import functional_classes.*;

import java.util.Objects;
import java.util.Scanner;


public class ClientMain {
    public static void main(String[] args) {
        String executedCommand;
        try {
            // initialization
            Store store = new Store();
            int port;
            do {
                System.out.println("Введите номер порта для этого клиентского приложения");
                port = Integer.parseInt(new Scanner(System.in).nextLine().trim());
            }  while (!store.addPort(port));

            ClientSerializer clientSerializer = new ClientSerializer(port);
            ClientReader reader = new ClientReader();
            Writer writer = new Writer();
            ClientManager clientManager = new ClientManager(clientSerializer, reader, writer);
            reader.setClientManager(clientManager);

            // execution

            writer.suggestNewAction();
            executedCommand = reader.readNextLine().trim();
            while (!Objects.equals(executedCommand, "exit")) {
                clientManager.startNewAction(executedCommand);
                writer.suggestNewAction();
                executedCommand = reader.readNextLine().trim();
            }
        } catch (NumberFormatException e) {
            System.out.println("Номер порта должен быть числом");
        } catch (Exception e){
            System.out.println(e);
        }
    }
}