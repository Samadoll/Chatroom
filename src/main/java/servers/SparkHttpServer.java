package servers;


import controllers.AccountController;
import controllers.ChatRoomController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

import static spark.Spark.*;

public class SparkHttpServer implements Runnable {

  private ChatRoomServer chatRoomServer;

  public SparkHttpServer(ChatRoomServer cs) {
    this.chatRoomServer = cs;
  }

  public void setup() {
    port(3000);
    staticFiles.externalLocation(System.getProperty("user.dir"));

    get("/", (req, res) -> {
      if (req.cookie("session_id") != null) {
        res.redirect("/app");
      } else {
        res.redirect("/chatroom/index");
      }
      return null;
    });
    try {
      Scanner scanner = new Scanner(new File(Paths.get(System.getProperty("user.dir"), "src", "main", "java", "routes.json").toString()));
      StringBuilder rawText = new StringBuilder();
      while (scanner.hasNext()) {
        rawText.append(scanner.next());
      }
      JSONObject routes = new JSONObject(rawText.toString());
      Iterator<String> keySet = (Iterator<String>) routes.keys();
      while (keySet.hasNext()) {
        String index = keySet.next();
        Object value = routes.get(index);
        if (value instanceof JSONArray) {
          JSONArray listOfPaths = (JSONArray) value;
          path(index, () -> {
            try {
              int pathLength = listOfPaths.length();
              for (int i = 0; i < pathLength; i++) {
                JSONObject jobj = listOfPaths.getJSONObject(i);
                String route = (String) jobj.keys().next();
                JSONObject data = jobj.getJSONObject(route);
                Method m = Spark.class.getDeclaredMethod(data.getString("method"), String.class, Route.class);

                m.invoke(null, route, (Route) (req, res) -> {
                  Method action = Class.forName("controllers." + data.getString("controller") + "Controller").getDeclaredMethod(data.getString("action"));
                  Object instance = Class.forName("controllers." + data.getString("controller") + "Controller")
                          .getConstructor(Request.class, Response.class, SparkHttpServer.class)
                          .newInstance(req, res, this);
                  action.invoke(instance);
                  return res.body();
                });
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
        } else {
          JSONObject data = routes.getJSONObject((String) value);

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    setup();
    init();
  }
}
