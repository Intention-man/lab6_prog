package client.functional_classes;

import client.auxiliary_classes.CommandMessage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class ClientSerializer {
    static DatagramSocket socket;
    static InetAddress host;
    static int clientPort;

    static DatagramChannel dc;
    static ByteBuffer buffer;
    static SocketAddress serverAddress;

    static {
        try {
            socket = new DatagramSocket();
            host = InetAddress.getByName("localhost");
            clientPort = 5000;
            serverAddress = new InetSocketAddress(host, 7000);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public static String send(CommandMessage commandMessage) {

//        byte[] arr = {0,1,2,3,4,5,6,7,8,9};

        // creation channel and open it
        try {
            dc = DatagramChannel.open();
            System.out.println(1);

            // byte object formation

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(commandMessage);

            byte[] byteBAOS = byteArrayOutputStream.toByteArray();
            buffer = ByteBuffer.wrap(byteBAOS);
            dc.send(buffer, serverAddress);
            System.out.println(2);
            dc.close();

            // space between sending and getting

            DatagramSocket socket = new DatagramSocket(clientPort);
            DatagramPacket packet = new DatagramPacket(byteBAOS, byteBAOS.length);
            socket.setSoTimeout(10000);
            socket.receive(packet);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBAOS);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            CommandMessage deserializedCommandMessage = (CommandMessage) objectInputStream.readObject();
            socket.close();
            return deserializedCommandMessage.getClassname();
//        return "work, you know?!)";
//        for (byte j : arr) {
//            System.out.println(j);
//        }
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            socket.close();
        }
        return "false";
    }
    public void close() {
        socket.close();
    }
}