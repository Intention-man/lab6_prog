package functional_classes;



import auxiliary_classes.CommandMessage;
import auxiliary_classes.FormField;
import enums.Country;
import enums.MovieGenre;
import enums.MpaaRating;
import movies_classes.Movie;
import movies_classes.Movies;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 *    Console App Component, responsible for console work and interaction with users
 *    It calls CollectionWorker's and FileWorker's methods
 *    It manages collection movies, as every functional components
 * */

public class Receiver {

    // initialization

    private static Movies movies;
    public static Scanner chosenScanner = new Scanner(System.in);
    static String executedCommand;

    private static List<String> executedFiles = new ArrayList<>();

    static HashMap<String, String> commandList = new HashMap<>();


    static ArrayList<FormField> form = new ArrayList<>();
    static HashMap<Integer, Object> answers = new HashMap<>();

    static {
        commandList.put("help", "вывести справку по доступным командам");
        commandList.put("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        commandList.put("show ", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        commandList.put("add {element}", "добавить новый элемент в коллекцию");
        commandList.put("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        commandList.put("remove_by_id id", "удалить элемент из коллекции по его id");
        commandList.put("clear", "очистить коллекцию");
        commandList.put("save", "сохранить коллекцию в файл");
        commandList.put("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        commandList.put("exit ", "завершить программу (без сохранения в файл)");
        commandList.put("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        commandList.put("add_if_min {element}", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        commandList.put("history", "вывести последние 12 команд (без их аргументов)");
        commandList.put("remove_any_by_oscars_count oscarsCount", "удалить из коллекции один элемент, значение поля oscarsCount которого эквивалентно заданному");
        commandList.put("sum_of_length", "вывести сумму значений поля length для всех элементов коллекции");
        commandList.put("count_by_oscars_count oscarsCount", "вывести количество элементов, значение поля oscarsCount которых равно заданному");

        form.add(new FormField(0, "String", true, "Введите название фильма"));
        form.add(new FormField(1, "Integer", true, "Введите координату x (это значение должно быть целым и больше -319)"));
        form.add(new FormField(2, "int", true, "Введите координату y"));
        form.add(new FormField(3, "long", true, "Введите количество оскаров у этого фильма"));
        form.add(new FormField(4, "long", true, "Введите длину фильма"));
        form.add(new FormField(5, "MovieGenre", false, "Введите жанр фильма: " + Arrays.asList(MovieGenre.values())));
        form.add(new FormField(6, "MpaaRating", false, "Введите рейтинг фильма:" + Arrays.asList(MpaaRating.values())));
        form.add(new FormField(7, "String", true, "Введите имя оператора"));
        form.add(new FormField(8, "String", true, "Введите данные паспорта оператора"));
        form.add(new FormField(9, "Country", false, "Введите национальность оператора: " + Arrays.asList(Country.values())));
        form.add(new FormField(10, "long", false, "Введите местоположение оператора (координата x)"));
        form.add(new FormField(11, "long", false, "Введите местоположение оператора (координата y)"));
        form.add(new FormField(12, "double", false, "Введите местоположение оператора (координата z)"));
    }

    public static void manageClass(Movies movies) {
        Receiver.movies = movies;
    }

    // common actions

    public static void suggestNewAction() {
//        System.out.println();
        System.out.println("Введите команду. Чтобы узнать перечень доступных команд ввелите help");
        executedCommand = chosenScanner.nextLine();
    }

    public static void startNewAction(String executedCommand) throws Exception {
        try {
            CollectionWorker.addCommandToHistory(executedCommand.split(" ")[0]);
            if (Objects.equals(executedCommand.split(" ")[0], "execute_script") && !chosenScanner.equals(new Scanner(System.in)) && executedFiles.contains(executedCommand.split(" ")[1])) {
                System.out.println("Рекурсия! Страшнааааа... Но это тоже обработано, уберите запуск одного и того же файла более чем 1 раз в рекурсивной цепочке и продолжайте работу)");
                return;
            } else if (Objects.equals(executedCommand.split(" ")[0], "execute_script") && (chosenScanner.equals(new Scanner(System.in)) || !executedFiles.contains(executedCommand.split(" ")[1]))) {
                Path path = Paths.get(executedCommand.split(" ")[1]);
                chosenScanner = new Scanner(path);
                executedFiles.add(executedCommand.split(" ")[1]);
            }
            switch (executedCommand.split(" ")[0]) {
                case ("help") -> help();
                case ("info") -> info();
                case ("add") -> CollectionWorker.addMovie(readInputNewMovieData());
                case ("add_if_max") -> CollectionWorker.addIfMax(readInputNewMovieData());
                case ("add_if_min") -> CollectionWorker.addIfMin(readInputNewMovieData());
                case ("clear") -> CollectionWorker.clear();
                case ("history") -> {

                    CommandMessage<Object> commandMessage = new CommandMessage<>("CollectionWorker", "getLast12Commands", null);
                    System.out.println(ClientSerializer.send(commandMessage));
                }
                case ("execute_script") -> FileWorker.readFile(chosenScanner, executedCommand.split(" ")[1]);
                case ("save") -> FileWorker.save();
                case ("show") -> show();
                case ("sum_of_length") -> System.out.println(CollectionWorker.sumOfLength());
                case ("count_by_oscars_count") ->
                        System.out.println(CollectionWorker.countByOscarsCount(Long.parseLong(executedCommand.split(" ")[1])));
                case ("remove_by_id") -> {
                    if (executedCommand.matches("remove_by_id \\d*")) {
                        if (!CollectionWorker.removeById(Integer.parseInt(executedCommand.split(" ")[1]))) {
                            System.out.println("Фильма с таким id нет в коллекции");
                        };
                    } else {
                        System.out.println("id должно быть целым числом");
                    }
                }
                case ("remove_any_by_oscars_count") -> {
                    if (executedCommand.matches("remove_any_by_oscars_count \\d*")) {
                        if (!CollectionWorker.removeAnyByOscarsCount(Long.parseLong(executedCommand.split(" ")[1]))) {
                            System.out.println("В коллекци нет ни 1 фильма с таким количеством оскаров");
                        }
                    } else {
                        System.out.println("Количество оскаров должно быть целым числом");
                    }
                }
                case ("update") -> {
                    if (executedCommand.matches("update \\d*") && Integer.parseInt(executedCommand.split(" ")[1]) >= 0) {
                        if (!CollectionWorker.updateMovie(Integer.parseInt(executedCommand.split(" ")[1]), readInputNewMovieData())) {
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

    // commands execution
    
    public static HashMap<Integer, Object> readInputNewMovieData() {
        int step = 0;
        while (step < Receiver.form.size()) {
            try {
                System.out.println(Receiver.form.get(step).getLabel() + ". Тип этого значения: " + Receiver.form.get(step).getExpectedType() + (Receiver.form.get(step).getIsNecessary() ? ". Обязательное значение" : ". Необязательное значение"));
                String line = chosenScanner.nextLine().trim();
                if (line.equals("exit")) {System.exit(0);}
                if (line.length() == 0 && Receiver.form.get(step).getIsNecessary()) {
                    System.out.println("Значение не может быть пустым");
                    continue;
                } else {
                    if (line.length() == 0) {
                        answers.put(step, null);
                        step += 1;
                        continue;
                    }
                }
                switch (Receiver.form.get(step).getExpectedType()) {
                    case ("Integer"), ("int") -> {
                        int parsedValue = Integer.parseInt(line);
                        if (form.get(step).getKey() == 1 && parsedValue <= -319) {
                            System.out.println("Значение должно быть больше -319");
                        } else {
                            answers.put(step, parsedValue);
                            step += 1;
                        }
                        answers.put(step, parsedValue);
                    }
                    case ("long") -> {
                        long parsedValue = Long.parseLong(line);
                        if ((form.get(step).getKey() == 3 || form.get(step).getKey() == 4) && parsedValue <= 0) {
                            System.out.println("Значение должно быть больше нуля");
                        } else {
                            answers.put(step, parsedValue);
                            step += 1;
                        }

                    }
                    case ("double") -> {
                        double parsedValue = Double.parseDouble(line);
                        answers.put(step, parsedValue);
                        step += 1;
                    }
                    case ("String") -> {
                        if ((form.get(step).getKey() == 0 || form.get(step).getKey() == 7 || form.get(step).getKey() == 8) && line.trim().isEmpty()) {
                            System.out.println("Значение не может быть пустым");
                        } else {
                            if (form.get(step).getKey() == 8 && line.length() < 9) {
                                System.out.println("Значение должно состоять не менее чем из 9 символов");
                            } else {
                                answers.put(step, line);
                                step += 1;
                            }
                        }

                    }
                    case ("MovieGenre") -> {
                        MovieGenre parsedValue = Enum.valueOf(MovieGenre.class, line);
                        answers.put(step, parsedValue);
                        step += 1;
                    }
                    case ("MpaaRating") -> {
                        MpaaRating parsedValue = Enum.valueOf(MpaaRating.class, line);
                        answers.put(step, parsedValue);
                        step += 1;
                    }
                    case ("Country") -> {
                        Country parsedValue = Enum.valueOf(Country.class, line);
                        answers.put(step, parsedValue);
                        step += 1;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите значение правильного типа данных: " + Receiver.form.get(step).getExpectedType());
            } catch (IllegalArgumentException e) {
                System.out.println("Введите значение из списка допустимых значений ->");
            }
        }
        return answers;
    }

    public static void help() {
        for (var entry : commandList.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public static void info() {
        System.out.println("Класс элементов коллекции: " + movies.getClass());
        System.out.println("Дата и время ининциализации коллекции: " + movies.getInitializationDate());
        System.out.println("Количество элементов в колллекции: " + movies.moviesCount());

        System.out.println("Список имеющихся в коллекции фильмов (id + название)");

        for (Movie movie : movies.getSortedMovies()) {
            System.out.println(movie.getId() + " - " + movie.getName());
        }
        System.out.println("Исполняемые в данный момент файлы: " + getExecutedFiles());
    }

    public static void show() {
        for (Movie movie : movies.getSortedMovies()) {
            for (String s : movie.getInstance()) {
                System.out.println(s);
            }
        }
    }

    // getters

    public static String getExecutedCommand() {
        return executedCommand;
    }

    public static List<String> getExecutedFiles() {
        return executedFiles;
    }
}