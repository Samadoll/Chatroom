package servers;

import controllers.ChatRoomController;
import controllers.Controller;
import controllers.LogInController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ray on 2018/2/25.
 */
public class Router {

  // Singleton design pattern instance
  public static Router router = null;

  // A table storing the path -> controller mapping
  private Map<String, Class> routeTable;

  // Singleton pattern helper method
  public static Router getInstance() {
    if (router == null) {
      router = new Router();
    }
    return router;
  }

  private Router() {
    routeTable = new HashMap<String, Class>();
    routeTable.put("/index", ChatRoomController.class);
    routeTable.put("/login", LogInController.class);
  }

  // method to route a request to the corresponding route table
  public void route(Socket request) {
    try {
      Map<String, String> params = new HashMap<String, String>();
      analyze(request, params);
      Class controller = routeTable.get(params.get("path"));

      // open a new thread for a new controller to handle the request
      new Thread((Controller) controller.getConstructor().newInstance(params, request)).start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // analyze the incoming HTTP request and store all the info in a params hashmap
  private void analyze(Socket request, Map<String, String> params) throws IOException {
    InputStream in = new BufferedInputStream(request.getInputStream());
    StringBuffer content = new StringBuffer();
    while (true) {
      int c = in.read();
      if (c == '\r' || c == '\n' || c == -1) {
        break;
      }
      content.append((char) c);
    }
    String rawRequest = content.toString();
  }


}
