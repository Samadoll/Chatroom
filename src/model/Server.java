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
 * Created by SamaCready on 16/10/20.
 */
/**
    Simplified??
*/
public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<Socket, String> usersList = new HashMap<>();
    private Map<String, String> usersnp = new HashMap<>();
    private boolean loggedna;
    private boolean loggedpw;
    private boolean logged;


    public Server() {
        this.loggedna = false;
        this.loggedpw = false;
        this.logged = false;
        loadUsers();

        try {
            serverSocket = new ServerSocket(12345);
            while (true) {
                socket = serverSocket.accept();
                // need to login or register
                LoginOrRegister();
                //usersList.put(socket, "Sama");
                if (isLogged()) {
                    System.out.println("Online: " + this.usersList.size());
                    new Thread(new Chatroom(socket, usersList)).start();
                }
                this.logged = false;
                this.loggedpw = false;
                this.loggedna = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        usersnp.put("Sama", "123");
        usersnp.put("Kori", "123");
    }

    private void LoginOrRegister() {
        try {
            BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
            String word = "";
            if (!isLogged()) {
                word = tempIn.readLine();
                String name = "";
                String pw = "";

                if (!word.equals("1") && !word.equals("2")) {
                    tempOut.println("invalid");
                    socket.close();
                }

                if (word.equals("2")) {
                    while (true) {
                        if (!isLoggedna()) {
                            tempOut.println("New Username: ");
                            name = tempIn.readLine();
                            this.loggedna = true;
                            continue;
                        } else if (!isLoggedpw()) {
                            tempOut.println("New Password: ");
                            pw = tempIn.readLine();
                            this.loggedpw = true;
                            this.usersnp.put(name, pw);
                            this.logged = true;
                            continue;
                        }
                        if (isLogged()) {
                            tempOut.println("true");
                            this.usersList.put(socket, name);
                            this.usersnp.put(name, pw);
                            tempOut.println("Welcome " + name + ".");
                            break;
                        }
                    }
                } else if (word.equals("1")) {
                    int count = 0;
                    while (true) {
                        if (!isLoggedna()) {
                            tempOut.println("Username: ");
                            name = tempIn.readLine();
                            if (this.usersnp.containsKey(name) && !this.usersList.containsValue(name)) {
                                this.loggedna = true;
                            }
                            if (count == 4) {
                                tempOut.println("too many times.");
                                break;
                            }
                            count ++;
                            continue;
                        } else if (!isLoggedpw()) {
                            tempOut.println("Password: ");
                            pw = tempIn.readLine();
                            if (this.usersnp.get(name).equals(pw)) {
                                this.loggedpw = true;
                                this.usersnp.put(name, pw);
                                this.logged = true;
                            }
                            if (!isLogged()) {tempOut.println("false");}
                            continue;
                        }
                        if (isLogged()) {
                            tempOut.println("true");
                            this.usersList.put(socket, name);
                            tempOut.println("Welcome " + name + ".");
                            break;
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLogged() { return this.logged;}
    private boolean isLoggedna() { return this.loggedna;}
    private boolean isLoggedpw() { return this.loggedpw;}

    public static void main(String[] args) { new Server();}
}
