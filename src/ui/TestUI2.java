package ui;

import model.Client3;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TestUI2 extends JFrame implements Observer{
    private JPanel contentPane;
    private JButton sendButton;
    private JButton priavteButton;
    private JTextArea enterArea;
    private JList<String> onList;
    private JList<String> pList;
    private JTextArea chatArea;
    private JButton selectButton;
    private Client3 client3;
    private DefaultListModel<String> onlineModel = new DefaultListModel<>();
    private DefaultListModel<String> privateModel = new DefaultListModel<>();
    private boolean isPrivateChat;
    private String privateChatUsers;

    public TestUI2() {
        super("ChatRoom");
        setContentPane(contentPane);
        this.setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.isPrivateChat = false;
        this.privateChatUsers = "";
        onCreate();
    }

    private void onCreate() {
        chatArea.setEditable(false);
        chatArea.setVisible(true);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        enterArea.setEditable(true);
        enterArea.setVisible(true);
        enterArea.setLineWrap(true);
        enterArea.setWrapStyleWord(true);
        enterArea.addKeyListener(new KeyAdapter() {

            boolean ctrlPressed;
            boolean enterPressed;

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = true;
                }
                if (ctrlPressed && enterPressed) {
                    try {
                        if (!enterArea.getText().equals(""))
                            if (isPrivateChat()) {
                                client3.sendMsg("/To" + privateChatUsers + ":"+ enterArea.getText().trim());
                            } else {
                                client3.sendMsg(enterArea.getText().trim());
                            }
                    } catch (IOException e1) {
                    }
                    enterArea.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = false;
                }
            }
        });


        sendButton.setVisible(true);
        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!enterArea.getText().equals(""))
                        if (isPrivateChat()) {
                            client3.sendMsg("/To" + privateChatUsers + ":"+ enterArea.getText());
                        } else {
                            client3.sendMsg(enterArea.getText());
                        }
                } catch (IOException e1) {
                }
                enterArea.setText("");
            }
        });

        onList.setModel(onlineModel);
        pList.setModel(privateModel);

        priavteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPrivateChat())
                    setPrivateChat(false);
                else
                    setPrivateChat(true);
                chatArea.append("\n\nPrivate Chat opened: " + isPrivateChat());
            }
        });


        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(String name: onList.getSelectedValuesList())
                    if (!privateModel.contains(name))
                        privateModel.addElement(name);
                for(String name: pList.getSelectedValuesList())
                    privateModel.removeElement(name);
                onList.clearSelection();
                pList.clearSelection();
                /**
                 * update Private chat list
                 */
                privateChatUsers = "";
                for(int i = 0; i < privateModel.size(); i++)
                    privateChatUsers += privateModel.get(i) + "/";
                privateChatUsers = privateChatUsers.substring(0, privateChatUsers.length() - 1);
                System.out.println(privateChatUsers);
            }
        });

    }

    public void setClient(Client3 client) {
        this.client3 = client;
    }

//    public static void main(String[] args) {
//        TestUI2 dialog = new TestUI2();
//        dialog.pack();
//        dialog.setVisible(true);
//    }


    public boolean isPrivateChat() {
        return isPrivateChat;
    }

    public void setPrivateChat(boolean privateChat) {
        isPrivateChat = privateChat;
    }

    @Override
    public void update(Observable o, Object arg) {

        switch ((String) arg) {
            case "/Registered":
                JOptionPane.showMessageDialog(this, "Congratulation! You Have Finished Your Registration, Now Heading To Login");
                break;
            case "/Show":
                setVisible(true);
                chatArea.setText(welcomeWords());
                break;
            case "/onlineList/":
                Client3 client3 = (Client3) o;
                List<String> userList = client3.getUserList();
                onlineModel.clear();
                for (String name : userList) {
                    if (!onlineModel.contains(name))
                        onlineModel.addElement(name);
                }
                break;
            default:
                Date now = new Date();
                if (((String) arg).contains(":")) {
                    String[] word = ((String) arg).split(":", 2);
                    chatArea.append("\n\n" + word[0] + " <" + now + ">" + ":\n" + word[1]);
                } else {
                    chatArea.append("\n\n" + "<" + now + ">" + "\n" + arg);
                }
                chatArea.setCaretPosition(chatArea.getText().length());
        }
    }


    private String welcomeWords() {
        return "Function Key: \n" +  "/online to check who's online \n"
                + "/ToXXXX: or /ToXXX/XXX: to sendButton private message to one user or mulitple users \n"
                + "Welcome. Chat now.";

    }
}
