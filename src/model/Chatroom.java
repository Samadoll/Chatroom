package model;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * Created by SamaCready on 16/10/20.
 */
public class Chatroom implements Runnable {
    private Socket socket;
    private Map<Socket, String> userlist;
    private BufferedReader in;
    private String username;
    private PrintWriter onlyWord;

    public Chatroom(Socket socket, Map<Socket, String> aList) {
        super();
        this.socket = socket;
        this.userlist = aList;
        this.username = aList.get(this.socket);
        this.onlyWord = this.outToSocket(this.socket);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PrintWriter outToSocket(Socket socket) {
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStreamWriter outWriter = new OutputStreamWriter(out);
        BufferedWriter bufferOut = new BufferedWriter(outWriter);
        return new PrintWriter(bufferOut, true);
    }

    private void broadcast(String msg) {
        for (Socket s : userlist.keySet()) {
            if (this.username.equals(userlist.get(s))) continue;
            PrintWriter wordout = this.outToSocket(s);
            wordout.println(msg);
        }
    }

    private void privateBroadcast(String msg, String[] manyusernames) {
        for (Socket s : userlist.keySet()) {
            for (String username : manyusernames) {
                if (username.equals(userlist.get(s))) {
                    this.outToSocket(s).println(msg);
                }
            }
        }
    }

    private void whoIsOnline() {
        String word = "";
        for (String s : userlist.values()) {
            word += s + ". ";
        }
        PrintWriter out = this.outToSocket(this.socket);
        out.println("Online: " + word + "Amount: " + userlist.values().size());
    }

    private void setNotify() {
    }


    @Override
    public void run() {
        String word = "";
        String sentence = "";
        String clientName = this.username;
        try {
            this.broadcast(clientName + " logged.");
            while ((word = this.in.readLine()) != null) {
//                if (!word.toLowerCase().equals("/exit")) {
//                    sentence = "from " + clientName + ": " + word;
//                    System.out.println(sentence);
//                    broadcast(sentence);
//                    continue;
//                }
//                break;
                switch (word.toLowerCase()) {
                    case "/exit":
                        break;
                    case "/online":
                        whoIsOnline();
                        continue;
                    case "/privatechat":
                        privateChat();
                        break;

                    default:
                        sentence = "from " + clientName + ": " + word;
                        System.out.println(sentence);
                        if (word.matches("/To(([a-z]|[A-Z])+/)*([a-z]|[A-Z])+:.+")) {

                            String privateusername = word.substring(3, word.indexOf(":"));

                            sentence = "from " + clientName + ": " + word.substring(word.indexOf(":") + 1);
                            onlyWord.println("You to " + privateusername + ": " + word.substring(word.indexOf(":") + 1));
                            privateBroadcast(sentence, privateusername.split("/"));


                        } else {
                            onlyWord.println("You to All: " + word);
                            broadcast(sentence);
                        }
                }

            }
            this.onlyWord.close();
            this.in.close();
            this.socket.close();
            this.userlist.remove(this.socket);
            this.broadcast(clientName + " left.");
            System.out.println("Client " + clientName + " left. Remaining: " + this.userlist.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void privateChat() {


    }


}

