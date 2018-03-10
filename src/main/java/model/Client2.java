package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by SamaCready on 16/10/23.
 */
public class Client2 {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    private boolean logged;

    public Client2() {
        this.logged = false;
        try {
            socket = new Socket("localhost", 12345);
            setChoice();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isLogged()) {
            startChat();
        }
    }

    private void startChat() {
        welcomeWords();
        new outPut().start();
        new inPut().start();
    }

    private void setChoice() throws IOException {
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1.Login     2.Register");
        String in = userIn.readLine();
        switch (in) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            default:
                System.out.println("invalid.");
                this.socket.close();
                userIn.close();
        }
    }

    private void login() throws IOException {
        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int remainingTimes = 5;

        while (true) {
//            String choice = "";
            String un = "";
            String pw = "";
            String back = "";
//            System.out.println("1.Login     2.Register");
//            choice = br.readLine();
            System.out.println("Username: ");
            un = br.readLine();
            System.out.println("Password: ");
            pw = br.readLine();

            tempOut.println(un);
            tempOut.println(pw);
            tempOut.println("1");
            back = tempIn.readLine();
            remainingTimes --;

            if (back.equals("true")) {
                this.logged = true;
                break;
            } else {
                System.out.println("Login fails. Please Try Again");
            }

            if (remainingTimes == 0) {
                System.out.println("Too many attempts.");
                tempIn.close();
                tempOut.close();
                this.socket.close();
                break;
            }
        }
    }

    private void register() throws IOException {
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (true) {
            String name = "";
            String pw = "";
            String back = "";

            System.out.println("Please Input Your New Username, It Can Only Be Alphabets.");
            name = userIn.readLine();

            while (!name.matches("([A-Z]|[a-z])+")) {
                System.out.println("Sorry, Your New Username Is Bad, Please Make A New One.");
                name = userIn.readLine();
            }

            System.out.println("Please Input Your New Password, It Must Be Longer Than 3");
            pw = userIn.readLine();

            while (pw.length() < 3) {
                System.out.println("Your Password Is Too Short, Please Try Again.");
                pw = userIn.readLine();
            }

            tempOut.println(name);
            tempOut.println(pw);
            tempOut.println("2");
            back = tempIn.readLine();
            if (back.equals("true")) break;
            System.out.println("Sorry, the username already exists. Please Try Again.");
        }

        System.out.println("Congratulation! You Have Finished Your Registration, Now Heading To Login");
        login();
    }


    private void welcomeWords() {
        System.out.println("Function Key: ");
        System.out.println("/exit to exit");
        System.out.println("/online to check who's online");
        System.out.println("/privatechat to make up a private conversation to someone");
        System.out.println("/ToXXXX: or /ToXXX/XXX: to send private message to one user or mulitple users");
        System.out.println("Welcome. Chat now.");
    }

    private class outPut extends Thread {
        public outPut() {
            super();
        }

        public void run() {
            try {
                BufferedReader userword = new BufferedReader(new InputStreamReader(System.in));
                out = new PrintWriter(socket.getOutputStream(), true);
                String word = null;
                while ((word = userword.readLine()) != null) {
                    if (word.toLowerCase().equals("/exit")) {break;}
                    out.println(word);
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }

    private class inPut extends Thread {
        public inPut() {
            super();
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inword = null;
                while ((inword = in.readLine()) != null) {
                    if (inword.toLowerCase().trim().equals("/exit")) { break;}
                    Date now = new Date();
                    System.out.println(now);
                    System.out.println(inword);
                    System.out.println();
                }
                in.close();
            } catch (IOException e) {
                ;
            }
        }
    }

    private boolean isLogged() { return this.logged;}

    public static void main(String[] args) {
        new Client2();
    }
}
