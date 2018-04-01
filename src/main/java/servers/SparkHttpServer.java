package servers;

import controllers.ChatRoomController;
import controllers.AccountController;

import static spark.Spark.*;

public class SparkHttpServer implements Runnable {

  public void setup() {
    port(3000);
    staticFiles.externalLocation(System.getProperty("user.dir"));
    path("/chatroom", () -> {
      get("/index", (req, res) -> {
        new ChatRoomController(req, res).index();
        return res.body();
      });

      get("/login_authenticate", (req, res) -> {
        new AccountController(req, res).login();
        return res.body();
      });

      post("/register", (req, res) -> {
        new AccountController(req, res).register();
        return res.body();
      });

      get("/app", (req, res) -> {
        new ChatRoomController(req, res).initChatRoom();
       return res.body();
      });
    });

  }

  @Override
  public void run() {
    setup();
    init();
  }
}
