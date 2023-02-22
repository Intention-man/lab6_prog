package server.functional_classes;

import client.auxiliary_classes.CommandMessage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ServerSerializer{
    private static boolean running;
    private static byte[] byteCommandMessage = new byte[256];
    static InetAddress host;
    static int serverPort = 7777;
    private static DatagramSocket socket;
    static SocketAddress socketAddress;

    static {
        try {
            socket = new DatagramSocket(serverPort);
            socketAddress = new InetSocketAddress(7000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    public static void run() {
        running = true;
        try {
            while (running) {
                DatagramChannel datagramChannel;
                ByteBuffer buffer;
                try {
                    datagramChannel = DatagramChannel.open();
                    datagramChannel.bind(socketAddress);
                    System.out.println(1);

//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    ObjectOutputStream oos = new ObjectOutputStream(bos);
//                    oos.writeObject(commandMessage);

                    buffer = ByteBuffer.wrap(byteCommandMessage);
                    socketAddress = datagramChannel.receive(buffer);
                    ByteArrayInputStream bis = new ByteArrayInputStream(byteCommandMessage);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    CommandMessage deserializedCommandMessage = (CommandMessage) ois.readObject();
                    deserializedCommandMessage.setClassname("right changed class!");
                    System.out.println(deserializedCommandMessage.getClassname());
                    datagramChannel.close();

                    // space between sending and getting

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(deserializedCommandMessage);
                    byte[] byteBAOS = byteArrayOutputStream.toByteArray();
                    buffer = ByteBuffer.wrap(byteBAOS);

                    host = InetAddress.getByName("localhost");
                    DatagramPacket packet = new DatagramPacket(byteBAOS, byteBAOS.length, host, 5000);
                    socket.send(packet);
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            socket.close();
        }
    }
}