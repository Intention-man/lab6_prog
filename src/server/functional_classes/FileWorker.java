package server.functional_classes;

import client.functional_classes.Receiver;
import server.movies_classes.Movies;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *    Console App Component, working with files: read, write and execute their.
 *    His methods called by Receiver.
 *    It manages collection movies, as every functional components.
 * */


public class FileWorker {

    // initialization

    private static Movies movies;
    static String xmlFileName = System.getenv("FileWithCollection");
    static Path path = Paths.get(xmlFileName);
    static Serializer serializer = new Persister();
    static Scanner scanner;

    static {
        try {
            scanner = new Scanner(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // common


    public static Movies fill() {
        try {
            return serializer.read(Movies.class, new File(xmlFileName));
        } catch (Exception e) {
            System.out.println("e");
        }
        return null;
    }

    public static void manageClass(Movies movies){
        FileWorker.movies = movies;
    }

    // commands execution


    public static void readFile(Scanner fileScanner, String fileName) {
        try {

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Receiver.startNewAction(line);
            }
            fileScanner.close();
            Receiver.getExecutedFiles().remove(fileName);
            Receiver.chosenScanner = new Scanner(System.in);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void save() {
        try {
            serializer.write(movies, new File(xmlFileName));
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении коллекции в файл. Узнайте у разработчика в чем дело)");
        }

    }

}
