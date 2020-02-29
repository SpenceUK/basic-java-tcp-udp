package com.spenceuk.server;

import static spark.Spark.get;
public class App {
  public static void main(String[] args) {
    get("/", (req, res) -> "Hello World!");
  }
}
