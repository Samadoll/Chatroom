package ui;

import javax.swing.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Samadoll on 2016-11-30.
 */
public class TestUI extends JFrame implements Observer {

    JTextArea inText;
    private Socket socket;


    public TestUI() {
        super("dsf");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(false);
        JPanel jPanel = new JPanel();
        this.add(jPanel);
        jPanel.setLayout(null);
        onCreate(jPanel);
        inText.setText("");
        //showLoginUI();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private void showLoginUI() {
        new LoginUI2();
    }

    @Override
    public void update(Observable o, Object arg) {

        switch ((String) arg) {
            case "/exit":
                System.exit(0);
                break;
            case "/Registered":
                //inText.setText("Congratulations! You have successfully registered.Now heading to Login ");
                JOptionPane.showMessageDialog(this, "Congratulation! You Have Finished Your Registration, Now Heading To Login");
                break;
            case "/Show":
                setVisible(true);
                inText.setText(welcomeWords());
                break;
            default:
                inText.append("\n"+(String) arg);
        }

    }

    private void onCreate(JPanel jPanel) {
        inText = new JTextArea();
        inText.setBounds(10, 10, 400, 300);
        inText.setEditable(false);
        inText.setVisible(true);

        jPanel.add(inText);
    }

    private String welcomeWords() {
        return "Function Key: \n" + "/exit to exit \n" + "/online to check who's online \n"
                + "/ToXXXX: or /ToXXX/XXX: to send private message to one user or mulitple users \n"
                + "Welcome. Chat now.";

    }
}
