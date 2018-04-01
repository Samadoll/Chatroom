package controllers;

import spark.Request;
import spark.Response;

import java.net.Socket;
import java.util.Map;

public class ChatRoomController extends Controller {

  public ChatRoomController(Request req, Response res) {
    super(req, res);
  }

  public void index() {
    render("html","/src/main/java/views/login_view.html");
  }

  public void initChatRoom() {

  }
}
