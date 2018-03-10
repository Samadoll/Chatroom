package servers;

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
 * Simplified??
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
        loggedna = false;
        loggedpw = false;
        logged = false;
        loadUsers();

        try {
            serverSocket = new ServerSocket(12345);
            while (true) {
                socket = serverSocket.accept();
                // need to login or register
                newLogginOrRegister();
                //LoginOrRegister();
                //usersList.put(socket, "Sama");
                if (isLogged()) {
                    System.out.println("Online: " + usersList.size());
                    new Thread(new Chatroom(socket, usersList)).start();
                }
                logged = false;
                loggedpw = false;
                loggedna = false;
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
                            count++;
                            continue;
                        } else if (!isLoggedpw()) {
                            tempOut.println("Password: ");
                            pw = tempIn.readLine();
                            if (this.usersnp.get(name).equals(pw)) {
                                this.loggedpw = true;
                                this.usersnp.put(name, pw);
                                this.logged = true;
                            }
                            if (!isLogged()) {
                                tempOut.println("false");
                            }
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

    private void newLogginOrRegister() throws IOException {

        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);

        if (!isLogged()) {

            String received = tempIn.readLine();

            if (received.equals("1")) {
                loggin();
            } else if (received.equals("2")) {
                register();
            } else {
                tempOut.println("Invalid");
                socket.close();
            }
        }
    }

    private void loggin() throws IOException {

        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);

        int remainingTimes = 4;
        String name = "";
        String pw = "";

        while (remainingTimes > 0) {
            tempOut.println("Username: ");
            name = tempIn.readLine();

            if (usersnp.containsKey(name) && !usersList.containsValue(name)) {
                loggedna = true;
                tempOut.println("Password: ");
                pw = tempIn.readLine();

                if (usersnp.get(name).equals(pw)) {
                    loggedpw = true;

                    if (loggedna == true && loggedpw == true) {
                        logged = true;
                        tempOut.println("true");
                        this.usersList.put(socket, name);
                        tempOut.println("Welcome " + name + ".");
                        break;
                    }
                } else {
                    remainingTimes++;
                }
            } else {
                tempOut.println("User DNE, Please Try Again");
                remainingTimes++;
            }
        }

        if (remainingTimes == 0) {
            tempOut.println("too many times.");
        }
    }

    private void register() throws IOException {

        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);

        String name = "";
        String pw = "";

        tempOut.println("Please Input Your New Username, It Can Only Be Alphabets");
        name = tempIn.readLine();

        while (!name.matches("[A-Z]+[a-z]+") || usersnp.containsKey(name)) {
            tempOut.println("Sorry, Your New Username Is Bad, Please Make A New One");
            name = tempIn.readLine();
        }

        tempOut.println("Please Input Your New Password, It Must Be Longer Than 3");
        pw = tempIn.readLine();

        while (pw.length() < 3) {
            tempOut.println("Your Password Is Too Short, Please Try Again");
            pw = tempIn.readLine();
        }

        usersnp.put(name, pw);
        tempOut.println("Congratulation! You Have Finished Your Registration, Now Heading To Loggin");
        loggin();
    }


    private boolean isLogged() {
        return logged;
    }

    private boolean isLoggedna() {
        return loggedna;
    }

    private boolean isLoggedpw() {
        return loggedpw;
    }

    public static void main(String[] args) {
        new Server();
    }
}
