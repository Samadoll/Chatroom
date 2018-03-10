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
    render("html","../views/hello.html");
  }
}
