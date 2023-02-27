package functional_classes;


import java.util.*;

public class ServerReader{
    public Scanner chosenScanner;

    public ServerReader() {
        chosenScanner = new Scanner(System.in);
    }

    public void readConsole() {
        if (chosenScanner.hasNextLine()) {
            System.out.println("smth wrong");
            if (chosenScanner.nextLine().trim().equals("exit")) {
                System.exit(0);
            }
        }
        System.out.println("smth wrong");
    }
}