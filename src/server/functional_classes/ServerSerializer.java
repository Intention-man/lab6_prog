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
    static int serverPortToSend = 7777;
    private static DatagramSocket socketToSend;
    static SocketAddress socketAddressToGet;
    static DatagramChannel datagramChannel;
    static ByteBuffer buffer;

    static {
        try {
            socketToSend = new DatagramSocket(serverPortToSend);
            socketAddressToGet = new InetSocketAddress(7000);
        } catch (SocketException e) {
            System.out.println(e);;
        }
    }


    public static void run() {
        running = true;
        try {
            while (running) {
                try {
                    datagramChannel = DatagramChannel.open();
                    datagramChannel.bind(socketAddressToGet);
                    System.out.println(1);

//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    ObjectOutputStream oos = new ObjectOutputStream(bos);
//                    oos.writeObject(commandMessage);

                    datagramChannel.receive(ByteBuffer.wrap(byteCommandMessage)); // ? снова socketAddress
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
//                    buffer = ByteBuffer.wrap(byteBAOS);
                    System.out.println(2);
                    host = InetAddress.getByName("localhost");
                    DatagramPacket packet = new DatagramPacket(byteBAOS, byteBAOS.length, host, 5000);
                    socketToSend.send(packet);
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            socketToSend.close();
        }
    }
}