package controllers;

import java.net.Socket;
import java.util.Map;

public class ChatRoomController extends Controller {

  public ChatRoomController(Map<String, String> params, Socket socket) {
    super(params, socket);
  }

  @Override
  public void run() {

  }
}
