import functional_classes.CollectionWorker;
import functional_classes.FileWorker;
import functional_classes.ServerSerializer;
import movies_classes.Movies;


public class ServerMain {
    public static void main(String[] args) {
        try {
//        Movies movies = new Movies();
            Movies movies = FileWorker.fill();
            assert movies != null;
            System.out.println(movies.getMovies().toString());
//
//            Receiver.manageClass(movies);
            CollectionWorker.manageClass(movies);
            FileWorker.manageClass(movies);
            ServerSerializer.run();
            // ServerSerializer.send();
        } catch (Exception | ExceptionInInitializerError e) {
            System.out.println(e);
        }
    }
}