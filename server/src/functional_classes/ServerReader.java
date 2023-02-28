package functional_classes;


import auxiliary_classes.CommandMessage;
import movies_classes.Movies;

import java.util.*;

public class ServerReader {
    public Scanner chosenScanner;
    CommandManager commandManager;

    public ServerReader(CommandManager commandManager) {
        chosenScanner = new Scanner(System.in);
        this.commandManager = commandManager;
    }

    public void readConsole() {
        if (chosenScanner.hasNextLine()) {
            if (chosenScanner.nextLine().trim().equals("exit")) {
                Movies result = commandManager.execution(new CommandMessage<>("CollectionWorker", "getMovies", null));
                boolean isSaved = commandManager.execution(new CommandMessage<>("FileWorker", "save", result));
                System.out.println(isSaved ? "Коллекция успешно сохранена!" : "Что-то пошло не так, коллекция не сохранена...");
                System.exit(0);
            }
        }
    }
}