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
        this.logged = true;
        try {
            socket = new Socket("localhost", 12345);
            login2();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isLogged()) {
            welcomeWords();
            new outPut().start();
            new inPut().start();
        }
    }

    private void login2() throws IOException {
        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String choice = "";
            String un = "";
            String pw = "";
            String back = "";
            System.out.println("1.Login     2.Register");
            choice = br.readLine();
            System.out.println("Username: ");
            un = br.readLine();
            System.out.println("Password: ");
            pw = br.readLine();

            tempOut.println(un);
            tempOut.println(pw);
            tempOut.println(choice);
            back = tempIn.readLine();
            if (back.equals("true")) {
                this.logged = true;
                break;

            }
        }
    }

    private void welcomeWords() {
        System.out.println("Function Key: ");
        System.out.println("/exit to exit");
        System.out.println("/online to check who's online");
        System.out.println();
        System.out.println("Chat now.");
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

    private boolean isLogged() { return logged;}

    public static void main(String[] args) {
        new Client2();
    }
}
