package servers;

import chathandlers.TextMessageHandler;
import model.Member;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
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

  public static final int SETUP = 1;
  public static final int MESSAGE = 2;
  private static final String TEXT = "text";
  private Member system;
  private volatile Map<Session, WebSocket> sessionMap;
  private Integer numberOnline;

  public ChatRoomServer(int port) {
    super(new InetSocketAddress(port));
  }

  public ChatRoomServer(InetSocketAddress address) {
    super(address);
  }

  public void addSession(UUID sessionID, Member member) {
    sessionMap.put(new Session(sessionID, member), null);
  }

  public void addSessoin(Session s) {
    sessionMap.put(s, null);
  }

  public String initialChatRoomState() {
    JSONArray members = new JSONArray();
    JSONObject initialState = null;
    try {
      initialState = new JSONObject()
              .put("type", SETUP)
              .put("numberOnline", numberOnline)
              .put("members", members);


    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return initialState.toString();
  }

  @Override
  public void onOpen(WebSocket ws, ClientHandshake clientHandshake) {
    String sessionID = ws.getResourceDescriptor().split("/")[1];
    Session verifiedSession = getSession(sessionID);
    sessionMap.put(verifiedSession, ws);
    ws.send(initialChatRoomState());
  }

  public Session getSession(String sessionId) {
    for (Session s : sessionMap.keySet()) {
      if (s.getSessionID().equals(sessionId))
        return s;
    }

    return null;
  }

  @Override
  public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
    ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
    String token = conn.getResourceDescriptor();
    if (token.equals("/") || getSession(token.split("/")[1]) == null) {
      throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Verification failed");
    }
    return builder;
  }

  @Override
  public void onClose(WebSocket ws, int i, String s, boolean b) {

  }

  @Override
  public void onMessage(WebSocket ws, String message) {
    try {
      JSONObject rawMessage = new JSONObject(message);
      String type = rawMessage.getString("type");
      switch (type) {
        case TEXT:
          new TextMessageHandler().process(rawMessage,this);
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
  public void run() {
    System.out.println("websocket server is now running on a new thread");
    super.run();
  }

  @Override
  public void onStart() {
    sessionMap = new ConcurrentHashMap<Session, WebSocket>();
    numberOnline = sessionMap.values().size();
    system = new Member("System");
    System.out.println("chatroom server setup finish!");
  }

  public class Session {

    private final UUID sessionID;
    private Member member;

    public Session(UUID sessionID, Member m) {
      this.sessionID = sessionID;
      this.member = m;
    }

    public UUID getSessionID() {
      return this.sessionID;
    }

    public Member getMember() {
      return this.member;
    }
  }
}
