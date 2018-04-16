package chathandlers;

import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;
import servers.ChatRoomServer;

public class TextMessageHandler {

  public void process(JSONObject content, ChatRoomServer server) throws JSONException {
    String sessionID = (String) content.remove("session_id");
    ChatRoomServer.Session speakerSession = server.getSession(sessionID);

    content.put("speaker", speakerSession.getMember().getName());
    server.broadcast(content.toString());
  }
}
