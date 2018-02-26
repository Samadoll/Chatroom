package servers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by ray on 2018/2/25.
 */
public class Server3 implements Runnable {

  private ServerSocket serverSocket;
  ChatRoomServer chatRoomServer;

  public Server3() {
    try {
      serverSocket = new ServerSocket(3000);
      chatRoomServer = new ChatRoomServer(883);
      new Thread(chatRoomServer).start();
      System.out.println("finish setup server");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
        Socket request = serverSocket.accept();
        InputStream in = new BufferedInputStream(request.getInputStream());
        StringBuffer content = new StringBuffer();
        while (true) {
          int c = in.read();
          if (c == '\r' || c == '\n' || c == -1) {
            break;
          }
          content.append((char) c);
        }
        System.out.println(content.toString());
        new Thread(new Router(content.toString()));

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Thread mainThread = new Thread(new Server3());
    mainThread.start();
  }
}
