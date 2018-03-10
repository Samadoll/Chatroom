package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.net.*;

/**
 * Created by SamaCready on 16/10/20.
 */
public class Client {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    private boolean logged;

    public Client() {
        this.logged = false;
        try {
            socket = new Socket("localhost", 12345);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("1.Login     2.Register");
        try {
            login();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isLogged()) {
            System.out.println("Function Key: ");
            System.out.println("/exit to exit");
            System.out.println("/online to check who's online");
            System.out.println();
            System.out.println("Chat now.");
            new outPut().start();
            new inPut().start();
        }
    }

    private void login() throws IOException {
        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        tempOut.println(br.readLine());

        while (!isLogged()) {
            String getin = tempIn.readLine();
            if (getin.equals("invalid")) { break;}
            if (getin.equals("too many times.")) {
                System.out.println(getin);
                break;
            }
            if (getin.contains("Username")) {
                System.out.println(getin);
                String userWord;
                if ((userWord = br.readLine()) != null)
                    tempOut.println(userWord);
            } else if (getin.contains("Password")) {
                System.out.println(getin);
                String userWord;
                if ((userWord = br.readLine()) != null) {
                    tempOut.println(userWord);
                    getin = tempIn.readLine();
                    if (getin.equals("true")) {
                        this.logged = true;
                        getin = tempIn.readLine();
                        System.out.println(getin);
                        break;
                    }
                }
                System.out.println(getin);
            }
        }
    }



    private boolean isLogged() { return logged;}

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
                    out.println(word);
                    if (word.toLowerCase().equals("/exit")) {break;}
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
                    Date now = new Date();
                    System.out.println(now);
                    System.out.println(inword);
                    System.out.println();
                    if (inword.toLowerCase().trim().equals("/exit")) { break;}
                }
                in.close();
            } catch (IOException e) {
                ;
            }
        }
    }

    public static void main(String[] args) { new Client();}

}
