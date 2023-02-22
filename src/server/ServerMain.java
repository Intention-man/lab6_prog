package server;

import client.functional_classes.Receiver;
import server.functional_classes.FileWorker;
import server.functional_classes.CollectionWorker;
import server.functional_classes.ServerSerializer;
import server.movies_classes.Movies;

import java.util.Objects;

public class ServerMain {
    public static void main(String[] args) {
        try {
//        Movies movies = new Movies();
            Movies movies = FileWorker.fill();
            assert movies != null;
            System.out.println(movies.getMovies().toString());
//
            Receiver.manageClass(movies);
            CollectionWorker.manageClass(movies);
            FileWorker.manageClass(movies);
            ServerSerializer.run();
            // ServerSerializer.send();
        } catch (Exception | ExceptionInInitializerError e) {
            System.out.println(e);
        }
    }
}