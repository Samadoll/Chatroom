package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SamaCready on 16/10/22.
 */

public class Server2 {
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<Socket, String> usersList = new HashMap<>();
    private Map<String, String> usersnp = new HashMap<>();
    private boolean logged;

    public Server2() {
        this.logged = false;
        loadUsers();

        try {
            serverSocket = new ServerSocket(12345);
            while (true) {
                socket = serverSocket.accept();
                Login();
                if (isLogged()) {
                    System.out.println("Online: " + this.usersList.size());
                    new Thread(new Chatroom(socket, usersList)).start();
                }
                this.logged = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        usersnp.put("Sama", "123");
        usersnp.put("Kori", "123");
    }

    private boolean isLogged() { return this.logged;}
    private void Login() {
        try {
            BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
            while (!isLogged()) {
                String un = "";
                String pw = "";
                String choice = "";

                un = tempIn.readLine();
                pw = tempIn.readLine();
                choice = tempIn.readLine();


                if (choice.equals("1") && checkCanLog(un, pw)) {
                    this.usersList.put(this.socket, un);
                    this.logged = true;
                    tempOut.println("true");
                    break;
                }

                if (choice.equals("2")) {
                    usersnp.put(un, pw);
                    this.usersList.put(this.socket, un);
                    this.logged = true;
                    tempOut.println("true");
                    break;
                }

                tempOut.println("false");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCanLog(String name, String pw) {
        if (!this.usersList.values().contains(name)) {
            return this.usersnp.get(name).equals(pw);
        }

        return false;
    }

    public static void main(String[] args) { new Server2();}

}
