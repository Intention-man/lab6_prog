package functional_classes;

public class Synchronizer {
    volatile boolean ready;
    int value;

    public void consoleThread(ServerReader serverReader) throws InterruptedException {
//        while (ready);
        synchronized(this) {
            serverReader.readConsole();
            ready = true;
        }
    }

    public void clientChannelThread(ServerSerializer serverSerializer) throws InterruptedException {
//        while (!ready);
        synchronized(this) {
            serverSerializer.correspondeWithClient();
            ready = false;
        }
    }
}
