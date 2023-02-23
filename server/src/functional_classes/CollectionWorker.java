package functional_classes;


import movies_classes.Movie;
import movies_classes.Movies;

import java.util.*;


/**
 *    Console App Component, executing most "backend" actions, related to the collection.
 *    His methods called by Receiver.
 *    It manages collection movies, as every functional components.
 * */

public class CollectionWorker {

    // initialization

    private static Movies movies;
    private static List<String> commandsHistory = new ArrayList<>();
    public static void manageClass(Movies movies) {
        CollectionWorker.movies = movies;}

    // commands execution

    public static void addMovie(HashMap data) {
        int currentCount = movies.getSortedMovies().get(movies.getSortedMovies().size() - 1).getId() + 1;
        movies.getMovies().add(new Movie(currentCount, data));
        System.out.println(movies.getMovies());
    }

    public static void addIfMax(HashMap data) {
        boolean isMax = true;
        for (Movie movie : movies.getMovies()) {
            if (movie.getLength() >= (long) data.get(4)) {
                isMax = false;
                break;
            }
        }
        if (isMax) {
            addMovie(data);
        }
    }

    public static void addIfMin(HashMap data) {
        boolean isMin = true;
        for (Movie movie : movies.getMovies()) {
            if (movie.getLength() <= (long) data.get(4)) {
                isMin = false;
                break;
            }
        }
        if (isMin) {
            addMovie(data);
        }
    }

    public static boolean updateMovie(int id, HashMap data) {
        for (Movie movie : movies.getMovies()) {
            if (movie.getId() == id) {
                movie.update(data);
                return true;
            }
        }
        return false;
    }

    public static int sumOfLength() {
        int sum = 0;
        for (Movie movie : movies.getMovies()) {
            sum += movie.getLength();
        }
        return sum;
    }

    public static int countByOscarsCount(long enteredCount) {
        int count = 0;
        for (Movie movie : movies.getMovies()) {
            if (movie.getOscarsCount() == enteredCount) {
                count++;
            }
        }
        return count;
    }

    public static boolean removeById(int enteredId) {
        for (Movie movie : movies.getMovies()) {
            if (movie.getId() == enteredId) {
                movies.getMovies().remove(movie);
                return true;
            }
        }
        return false;
    }

    public static boolean removeAnyByOscarsCount(long enteredCount) {
        for (Movie movie : movies.getMovies()) {
            if (movie.getOscarsCount() == enteredCount) {
                movies.getMovies().remove(movie);
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        movies.getMovies().clear();
    }

    public static void addCommandToHistory(String command){

        commandsHistory.add(command);
        System.out.println("new all commands in history: " + commandsHistory);
    }

    public static List<String> getLast12Commands() {
        System.out.println("print all:" + commandsHistory);
        return commandsHistory.subList(commandsHistory.size() >= 12 ? commandsHistory.size() - 12 : 0, commandsHistory.size());
    }
}