package com.spenceuk.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class App {
  public static void main(String[] args) throws SocketException {
    DatagramSocket socket = new DatagramSocket(9090);
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
  private DatagramSocket socket;
  private byte[] buffer = new byte[256];
  private DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
  private DatagramPacket response;
  private int sent = 0;

  public DatagramMessageSender(DatagramSocket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      socket.receive(packet);
      String msg = new String(packet.getData(), StandardCharsets.UTF_8);
      System.out.printf("Received: [%s:%s] - %s", packet.getAddress(), packet.getPort(), msg);
      while (sent < messages.size()) {
        byte[] messageBuffer = messages.get(sent).getBytes();
        response = new DatagramPacket(messageBuffer, messageBuffer.length, packet.getAddress(), packet.getPort());
        socket.send(response);
        sent++;
      }
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

}
