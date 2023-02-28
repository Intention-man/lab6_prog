package functional_classes;


import auxiliary_classes.CommandMessage;
import auxiliary_classes.ResponseMessage;

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
            socket = new DatagramSocket(clientPort);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public static ResponseMessage send(CommandMessage<Object> commandMessage) {

        // creation channel and open it
        try {
            dc = DatagramChannel.open();
            dc.configureBlocking(false);

            // byte object formation

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(commandMessage);

            byte[] byteBAOS = byteArrayOutputStream.toByteArray();
            buffer = ByteBuffer.wrap(byteBAOS);
            dc.send(buffer, serverAddress);
//            dc.close();

            // space between sending and getting
            byteBAOS = new byte[8192];
            DatagramPacket packet = new DatagramPacket(byteBAOS, byteBAOS.length);
            socket.setSoTimeout(10000);
            socket.receive(packet);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            ResponseMessage deserializedResponse = (ResponseMessage) objectInputStream.readObject();
            return deserializedResponse;
        } catch (SocketTimeoutException e){
            System.out.println("Убедитесь, что серверное приложение включено");
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        finally {
//            socket.close();
//        }
        return new ResponseMessage<>("null", null);
    }

}