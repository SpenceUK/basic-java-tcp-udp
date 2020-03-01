package com.spenceuk.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpClient {
  public static void main(String[] args) throws IOException {
    DatagramSocket socket = new DatagramSocket();

    byte[] buf = "Send Memes".getBytes();
    InetAddress address = InetAddress.getByName("127.0.0.1");
    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9090);
    socket.send(packet);

    while (!socket.isClosed()) {
      byte[] responseBuffer = new byte[1024];
      DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length);
      socket.receive(response);
      String received = new String(response.getData(), StandardCharsets.UTF_8);
      System.out.println("Received: " + received);
    }
    socket.close();
    System.out.println("Connection closed");
  }
}
