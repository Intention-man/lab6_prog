package functional_classes;

import auxiliary_classes.CommandMessage;
import auxiliary_classes.ResponseMessage;
;
import movies_classes.Movies;

import java.util.*;

/**
 * Console App Component, responsible for console work and interaction with users
 * It calls CollectionWorker's and FileWorker's methods
 * It manages collection movies, as every functional components
 */

public class ClientManager {

    // initialization

   ClientReader clientReader; Writer writer;

    public ClientManager(ClientReader clientReader, Writer writer) {
        this.clientReader = clientReader;
        this.writer = writer;
    }

    // main action

    public void startNewAction(String executedCommand) {
        try {
            executedCommand = executedCommand.trim();
            CommandMessage<Object> commandMessage;
            commandMessage = new CommandMessage<>("CollectionWorker", "addCommandToHistory", executedCommand.split(" ")[0]);
            ClientSerializer.send(commandMessage);

            switch (executedCommand.split(" ")[0]) {
                case ("add") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "addMovie", clientReader.readInputNewMovieData());
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("add_if_max") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "addIfMax", clientReader.readInputNewMovieData());
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("add_if_min") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "addIfMin", clientReader.readInputNewMovieData());
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("clear") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "clear", null);
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("count_by_oscars_count") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "countByOscarsCount", Long.parseLong(executedCommand.split(" ")[1]));
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("execute_script") -> clientReader.readFile(executedCommand.substring(15));
                case ("help") -> writer.help();
                case ("history") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "getLast12Commands", null);
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("info") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "info", null);
                    writer.printResponse(ClientSerializer.send(commandMessage));
                    System.out.println("Исполняемые в данный момент файлы: " + clientReader.getExecutedFiles());
                }
                case ("save") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "getMovies", null);
                    Movies movies = (Movies) ClientSerializer.send(commandMessage).getResponseData();
                    commandMessage = new CommandMessage<>("FileWorker", "save", movies);
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("show") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "show", null);
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("sum_of_length") -> {
                    commandMessage = new CommandMessage<>("CollectionWorker", "sumOfLength", null);
                    writer.printResponse(ClientSerializer.send(commandMessage));
                }
                case ("remove_by_id") -> {
                    if (executedCommand.matches("remove_by_id \\d*")) {
                        commandMessage = new CommandMessage<>("CollectionWorker", "removeById", Integer.parseInt(executedCommand.split(" ")[1]));
                        ResponseMessage response = ClientSerializer.send(commandMessage);
                        if (response.getResponseData().equals(true)) {
                            writer.printResponse(response);
                        } else {
                            System.out.println("Фильма с таким id нет в коллекции");
                        }
                    } else {
                        System.out.println("id должно быть целым числом");
                    }
                }
                case ("remove_any_by_oscars_count") -> {
                    if (executedCommand.matches("remove_any_by_oscars_count \\d*")) {
                        commandMessage = new CommandMessage<>("CollectionWorker", "removeAnyByOscarsCount", Long.parseLong(executedCommand.split(" ")[1]));
                        ResponseMessage response = ClientSerializer.send(commandMessage);
                        if (response.getResponseData().equals(true)) {
                            writer.printResponse(response);
                        } else {
                            System.out.println("В коллекци нет ни 1 фильма с таким количеством оскаров");
                        }
                    } else {
                        System.out.println("Количество оскаров должно быть целым числом");
                    }
                }
                case ("update") -> {
                    if (executedCommand.matches("update \\d*") && Integer.parseInt(executedCommand.split(" ")[1]) >= 0) {
                        HashMap<Integer, Object> map = clientReader.readInputNewMovieData();
                        map.put(map.size(), Integer.parseInt(executedCommand.split(" ")[1]));
                        commandMessage = new CommandMessage<>("CollectionWorker", "updateMovie", map);
                        ResponseMessage response = ClientSerializer.send(commandMessage);
                        if (response.getResponseData().equals(true)) {
                            writer.printResponse(response);
                        } else {
                            System.out.println("В коллекци нет ни 1 фильма с таким id (введите add, чтобы создать)");
                        }
                    } else {
                        System.out.println("id должно быть целым числом");
                    }
                }
                default -> System.out.println("Введите команду из доступного перечня");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}