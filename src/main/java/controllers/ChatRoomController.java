package controllers;

import servers.SparkHttpServer;
import spark.Request;
import spark.Response;

import java.net.Socket;
import java.util.Map;

public class ChatRoomController extends Controller {

  public ChatRoomController(Request req, Response res, SparkHttpServer server) {
    super(req, res, server);
  }

  public void index() {
    render("html","/src/main/java/views/login_view.html");
  }

  public void register() {
    render("html","/src/main/java/views/register_view.html");
  }

  public void initChatRoom() {
    render("html", "/src/main/java/views/chatroom_view.html");
  }
}
