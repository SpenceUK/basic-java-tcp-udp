package com.spenceuk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

public class App {
  public static void main(String[] args) throws IOException {
    ServerSocketFactory factory = ServerSocketFactory.getDefault();
    ServerSocket serverSock = factory.createServerSocket(8080);
    ExecutorService connPool = Executors.newCachedThreadPool();
    int threadId = 0;
    while (true) {
      connPool.execute(new ExchangeHandler(serverSock.accept(), threadId));
      threadId++;
    }
  }
}


class ExchangeHandler implements Runnable {
  private final Socket conn;
  private final String remote;
  private final int id;

  public ExchangeHandler(Socket clientSocket, int id) {
    this.conn = clientSocket;
    this.id = id;
    this.remote = conn.getRemoteSocketAddress().toString();
  }

  @Override
  public void run() {
    try (
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        PrintWriter pw = new PrintWriter(conn.getOutputStream(), true);
      ) {
        boolean receive = true;
        String input = null;
        while (receive) {
          input = br.readLine();
          System.out.printf("[conn-%s] R:[%s] - Received: %s\n", id, remote, input);
          if (input == null || input.isBlank()) receive = false;
        }
        if (input != null) {
          System.out.printf("[conn-%s] Sending Response\n", id);
          pw.println("HTTP/1.1 200 SUCCESS"); // status line
          pw.println("Content-Type: text/plain"); // headers
          pw.println("\r\n"); // required between headers and body
          pw.println("Hello world"); // body
          pw.println("\r\n"); // end of body
          System.out.printf("[conn-%s] Response Sent\n", id);
        }
        System.out.printf("[conn-%s] closing connection\n", id);
      } catch (Exception ex) {
        System.out.printf("[conn-%s] Connection Error: %s\n", id, ex.getMessage());
      }
    }
  }


