package model;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by SamaCready on 16/10/20.
 */
public class Chatroom  implements Runnable {
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
            passOnlinePeople();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PrintWriter outToSocket(Socket socket) {
        OutputStream out = null;
        try {
            if (!socket.isClosed())
                out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (out == null) return null;
        OutputStreamWriter outWriter = new OutputStreamWriter(out);
        BufferedWriter bufferOut = new BufferedWriter(outWriter);
        return new PrintWriter(bufferOut, true);
    }

    private void broadcast(String msg) {
        for (Socket s : userlist.keySet()) {
            if (this.username.equals(userlist.get(s))) continue;
            PrintWriter wordout = this.outToSocket(s);
            if (wordout == null) continue;
            wordout.println(msg);
        }
    }

    private void passOnlinePeople(){
        String totalPeople = "";
        for (String str : userlist.values()) {
            totalPeople = totalPeople + str + ". ";
        }
        for (Socket s : userlist.keySet()) {
            PrintWriter wordout = this.outToSocket(s);
            if (wordout == null) continue;
            wordout.println("/onlineList/" + totalPeople);
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



    @Override
    public void run() {
        String word = "";
        String flag;
        String sentence;
        String clientName = this.username;

        try {
            this.broadcast(this.username + " logged.\n/FlagFlag");
            passOnlinePeople();
            while ((flag = this.in.readLine()) != null) {
                word += flag + "\n";
                if (!flag.equals("/FlagFlag")) continue;
                word = word.substring(0, word.length() - 1);
                switch (word.toLowerCase()) {
                    case "/online":
                        whoIsOnline();
                        continue;
                    case "/privatechat":
                        privateChat();
                        break;

                    default:
                        sentence = "from " + clientName + ":" + word;
                        System.out.println(sentence);
                        /** Private Msg needs to rewrite
                         *  Regular Expression does not work
                         */
                        if (word.matches("/To(([a-z]|[A-Z])+/)*([a-z]|[A-Z])+:.+")) {

                            String privateusername = word.substring(3, word.indexOf(":"));

                            sentence = "from " + clientName + ":" + word.substring(word.indexOf(":") + 1);
                            onlyWord.println("You to " + privateusername + ": " + word.substring(word.indexOf(":") + 1));
                            privateBroadcast(sentence, privateusername.split("/"));


                        } else {
                            onlyWord.println("You to All:" + word);
                            broadcast(sentence);
                        }
                }
                word = "";
            }
            this.onlyWord.close();
            this.in.close();
            this.socket.close();
            this.userlist.remove(this.socket);
            this.broadcast(clientName + " left.");
            passOnlinePeople();
            System.out.println("Client " + clientName + " left. Remaining: " + this.userlist.size());
        } catch (Exception e) {
            userlist.remove(socket);
            this.broadcast(clientName + " left.");
            passOnlinePeople();
            System.out.println("Client " + clientName + " left. Remaining: " + this.userlist.size());
            e.printStackTrace();
        }

    }

    private void privateChat() {


    }


}

