package com.spenceuk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;


public class App {
  public static void main(String[] args) throws IOException {
    ServerSocketFactory factory = ServerSocketFactory.getDefault();
    ServerSocket serverSock = factory.createServerSocket(8080);

    while (true) {
      try (
        Socket clientSock = serverSock.accept();
        PrintWriter pw = new PrintWriter(clientSock.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
      ) {
        boolean receive = true;
        boolean res = true;
        String input;
        while (receive) {
          input = br.readLine();
          System.out.println(input);
          if (input == null || input.isBlank()) receive = false;
        }
        System.out.println("end read");
        if (res) {
          System.out.println("Begin Response");
          pw.println("HTTP/1.1 200 SUCCESS");
          pw.println("\r\n");
          pw.println("Hello");
        }
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
  }
}
