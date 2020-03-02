package com.spenceuk.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

public class UdpClient {
  public static void main(String[] args) throws IOException {
    InetAddress groupAddress = InetAddress.getByName("224.0.0.1");
    int count = 0;
    try (
      MulticastSocket socket = new MulticastSocket(9091);
    ) {
      socket.joinGroup(groupAddress);
      while (count < 30) {
        byte[] responseBuffer = new byte[1024];
        DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length);
        socket.receive(response);
        String received = new String(response.getData(), StandardCharsets.UTF_8);
        System.out.println("Received: " + received);
        Thread.sleep(1_000);
        count++;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
