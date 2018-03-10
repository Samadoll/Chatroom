package servers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by ray on 2018/2/25.
 */
public class Server3 {

  private SparkHttpServer httpServer;
  ChatRoomServer chatRoomServer;

  public Server3() {
      httpServer = new SparkHttpServer();
      chatRoomServer = new ChatRoomServer(883);
      new Thread(httpServer).start();
      new Thread(chatRoomServer).start();
      System.out.println("finish setup server");

  }

  public static void main(String[] args) {
    new Server3();
  }
}
