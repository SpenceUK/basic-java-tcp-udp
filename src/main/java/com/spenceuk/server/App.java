package com.spenceuk.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class App {
  public static void main(String[] args) throws IOException {
    MulticastSocket socket = new MulticastSocket(9091);
    Thread msgServerThread = new Thread(new DatagramMessageSender(socket));
    msgServerThread.start();
    while (true) {}
  }
}

class DatagramMessageSender implements Runnable {
  private List<String> messages = Arrays.asList(
    "Hello!",
    "These are Some..",
    "..Datagram packets",
    "Instead of Memes"
  );
  private MulticastSocket socket;
  private DatagramPacket response;

  public DatagramMessageSender(MulticastSocket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      System.out.println("Broadcasting");
      InetAddress group = InetAddress.getByName("224.0.0.1");
      while (true) {
        Thread.sleep(1_000);
        int random = new Random().nextInt(4);
        System.out.println("Sending ...");
        byte[] messageBuffer = messages.get(random).getBytes();
        response = new DatagramPacket(messageBuffer, messageBuffer.length, group, 9091);
        socket.send(response);
      }
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

}
