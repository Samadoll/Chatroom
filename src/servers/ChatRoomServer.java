package servers;

import model.Member;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    private Map<String, Member> tokens;
    private Integer numberOnline;

    public ChatRoomServer(int port) {
        super(new InetSocketAddress(port));
    }

    public ChatRoomServer(InetSocketAddress address) {
        super(address);
    }

    public void addTokens(String token, Member member) {
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
                members.put(new JSONObject().put("name", m.getName()).put("admin", m.isAdmin()));
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
        if (tokens.keySet().contains(ws.getRemoteSocketAddress().getAddress().getHostAddress())) {
            System.out.println("verification failed, this websocket has not been authorized");
            removeConnection(ws);
        } else {
            ws.send(initialChatRoomState());
        }
    }

    @Override
    public void onClose(WebSocket ws, int i, String s, boolean b) {
        
    }

    @Override
    public void onMessage(WebSocket ws, String s) {

    }

    @Override
    public void onError(WebSocket ws, Exception e) {

    }

    @Override
    public void onStart() {
        tokens = new ConcurrentHashMap<String, Member>();
        numberOnline = tokens.keySet().size();
    }
}
