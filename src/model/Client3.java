package model;

import ui.LoginUI2;
import ui.TestUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Observable;

/**
 * Created by SamaCready on 16/10/23.
 */
public class Client3 extends Observable{
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    private boolean logged;

    private TestUI testUI;
    private LoginUI2 loginUI2;

    public Client3(TestUI testUI) {
        this.testUI = testUI;
    }

    public void setLoginUI2(LoginUI2 loginUI2) {
        this.loginUI2 = loginUI2;
    }

    public void startChat() {
        welcomeWords();
        new outPut().start();
        new inPut().start();
    }

    private void welcomeWords() {
        System.out.println("Function Key: ");
        System.out.println("/exit to exit");
        System.out.println("/online to check who's online");
//        System.out.println("/privatechat to make up a private conversation to someone");
        System.out.println("/ToXXXX: or /ToXXX/XXX: to send private message to one user or mulitple users");
        System.out.println("Welcome. Chat now.");
    }

    public void login(String name, String pw, Socket socket) throws IOException {
        this.socket = socket;
        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
        tempOut.println(name);
        tempOut.println(pw);
        tempOut.println("1");
        String back = tempIn.readLine();

        if (back.equals("true")) {
            loginUI2.setVisible(false);
            testUI.setVisible(true);
            startChat();
            this.addObserver(testUI);

        } else {
            socket.close();
            tempIn.close();
            tempOut.close();
            throw new IOException("Login fails. Please Try Again");
        }
    }

    public void register(String name, String pw, Socket socket) throws IOException {
        if (!name.matches("([A-Z]|[a-z])+") || pw == null || pw.length() < 3) {
            throw new IOException("Sorry, Your New Username Is Bad, Please Make A New One.");
        } else {
            PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            tempOut.println(name);
            tempOut.println(pw);
            tempOut.println("2");
            String back = tempIn.readLine();
            if (back.equals("true"))
                JOptionPane.showMessageDialog(testUI, "Congratulation! You Have Finished Your Registration, Now Heading To Login");
            else
                throw new IOException("Sorry, the username already exists. Please Try Again.");
            tempIn.close();
            tempOut.close();
            socket.close();
        }
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
                    setChanged();
                    notifyObservers(word);
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
                    setChanged();
                    notifyObservers(inword);
                }
                in.close();
            } catch (IOException e) {
                ;
            }
        }
    }
}
