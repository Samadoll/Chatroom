package model;

import ui.LoginUI2;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created by SamaCready on 16/10/23.
 */
public class Client3 extends Observable {
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    private boolean logged;

    private LoginUI2 loginUI2;
    private List<String> dataBase;
    private List<String> userList;

    public Client3() {
        dataBase = new LinkedList<String>();
        userList = new LinkedList<String>();
    }

    public void setLoginUI2(LoginUI2 loginUI2) {
        this.loginUI2 = loginUI2;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void startChat() throws IOException {
        welcomeWords();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        new inPut().start();
    }


    private void welcomeWords() {
        System.out.println("Function Key: ");
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
            setChanged();
            notifyObservers("/Show");
            startChat();

        } else {
            loginUI2.setRemainingTry(loginUI2.getRemainingTry() - 1);
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
            if (back.equals("true")) {
                setChanged();
                notifyObservers("/Registered");
            } else {
                throw new IOException("Sorry, the username already exists. Please Try Again.");
            }
            tempIn.close();
            tempOut.close();
            socket.close();
        }
    }

    public void sendMsg(String msg) throws IOException {
        out.println(msg);
        out.println("/FlagFlag");

    }

//    private class outPut extends Thread {
//        public outPut() {
//            super();
//        }
//
//        public void run() {
//            try {
//                BufferedReader userword = new BufferedReader( new InputStreamReader(System.in));
//                out = new PrintWriter(socket.getOutputStream(), true);
//                String word = null;
//
//                while ((word = userword.readLine()) != null) {
//                    if (word.toLowerCase().equals("/exit")) {
//                        break;
//                    }
//                    //manageDatabase(word);
//                    //setChanged();
//                    //notifyObservers(word);
//                    out.println(word);
//                }
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                out.close();
//            }
//        }
//    }

    private class inPut extends Thread {
        public inPut() {
            super();
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inword = null;
                String flagword = "";
                while ((inword = in.readLine()) != null) {
                    if (inword.matches("\\/onlineList\\/(.+)")) {
                        String [] msg = inword.split("/");
                        String namewithdot = msg [2];
                        String [] onlineuser = namewithdot.split(". ");

                        userList = Arrays.asList(onlineuser);
                        setChanged();
                        notifyObservers("/onlineList/");
                        continue;
                    }

                    if (!inword.equals("/FlagFlag")) {
                        flagword += inword + "\n";
                        continue;
                    }
                    flagword = flagword.substring(0, flagword.length() - 1);
                    manageDatabase(flagword);
                    setChanged();
                    notifyObservers(flagword);
                    flagword = "";
                }
                in.close();
            } catch (IOException e) {
                ;
            }
        }
    }

    private void manageDatabase(String line) {

        dataBase.add(line);
        if (dataBase.size() > 100) {
            dataBase.remove(0);
        }
    }
}
