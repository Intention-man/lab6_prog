package functional_classes;

public class Synchronizer {
    volatile boolean ready;
    int value;

    public void consoleThread(ServerReader serverReader) throws InterruptedException {
            serverReader.readConsole();
    }

    public void clientChannelThread(ServerSerializer serverSerializer) throws InterruptedException {
        serverSerializer.correspondeWithClient();
    }
}
