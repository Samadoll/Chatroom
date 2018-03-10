package servers;

import model.Member;
import model.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the third version of the ChatRoom server,
 * which is our version of implementation of webSocketServer
 * from TooTallNate/Java-WebSocket. Again I would like to
 * acknowledge all the efforts Nathan and other developers have
 * made for this WebSocket.
 * <p>
 * Created by ray on 2018/2/25.
 */
public class ChatRoomServer extends WebSocketServer {

  public static final String LOG_OUT = "LOG_OUT";
  public static final String MESSAGE = "MESSAGE";
  private Member system;
  private Map<UUID, Member> tokens;
  private Integer numberOnline;

  public ChatRoomServer(int port) {
    super(new InetSocketAddress(port));
  }

  public ChatRoomServer(InetSocketAddress address) {
    super(address);
  }

  public void addToken(UUID token, Member member) {
    tokens.put(token, member);
  }

  public String initialChatRoomState() {
    JSONArray members = new JSONArray();
    JSONObject initialState = null;
    try {
      initialState = new JSONObject()
              .put("numberOnline", numberOnline)
              .put("members", members);

      for (Member m : tokens.values()) {
        JSONObject memberInfo = new JSONObject().put("name", m.getName());
        if (m.isAdmin()) memberInfo.put("admin", m.isAdmin());
        members.put(memberInfo);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return initialState.toString();
  }

  @Override
  public void onOpen(WebSocket ws, ClientHandshake clientHandshake) {
    // verification
    UUID requestToken = UUID.fromString(ws.getResourceDescriptor());
    if (tokens.keySet().contains(requestToken)) {
      System.out.println("verification failed, this websocket has not been authorized");
      removeConnection(ws);
    } else {
      Member currentMember = tokens.get(ws.getRemoteSocketAddress().getAddress().getHostAddress());
      currentMember.setWebSocket(ws);
      ws.send(initialChatRoomState());
    }
  }

  @Override
  public void onClose(WebSocket ws, int i, String s, boolean b) {
    try {
      String token = ws.getRemoteSocketAddress().getAddress().getHostAddress();
      Member logOutMember = tokens.remove(token);
      String logOutMessage = new Message(logOutMember.getName() + " has logged out from the chatroom.", system).toJSONSting();
      broadcast(logOutMessage);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onMessage(WebSocket ws, String s) {
    try {
      JSONObject rawMessage = new JSONObject(s);
      String type = rawMessage.getString("type");
      switch (type) {
        case LOG_OUT:
          removeConnection(ws);
          break;
        case MESSAGE:
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void onError(WebSocket ws, Exception e) {

  }

  @Override
  public void onStart() {
    tokens = new ConcurrentHashMap<UUID, Member>();
    numberOnline = tokens.keySet().size();
    system = new Member("System");
  }
}
