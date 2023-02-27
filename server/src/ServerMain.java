import functional_classes.*;
import movies_classes.Movies;


public class ServerMain {
    public static void main(String[] args) {
        try {
//        Movies movies = new Movies();
            FileWorker fileWorker = new FileWorker();
            Movies movies = fileWorker.fill();
            CollectionWorker collectionWorker = new CollectionWorker(movies);
            CommandManager commandManager = new CommandManager();
            commandManager.addExecutor("CollectionWorker", collectionWorker);
            commandManager.addExecutor("FileWorker", fileWorker);

            ServerReader serverReader = new ServerReader();
            ServerSerializer serverSerializer = new ServerSerializer(commandManager);

            run(serverSerializer, serverReader);

        } catch (Exception | ExceptionInInitializerError e) {
            System.out.println(e);
        }
    }

    public static void run(ServerSerializer serverSerializer, ServerReader serverReader) {
        try {
            Synchronizer synchronizer = new Synchronizer();
            Thread t1 = new Thread(() -> {
                try {
                    synchronizer.consoleThread(serverReader);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    synchronizer.clientChannelThread(serverSerializer);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println(1);
            t2.start();
            System.out.println(2);
            t1.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}