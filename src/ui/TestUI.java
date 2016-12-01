package ui;

import javax.swing.*;

/**
 * Created by Samadoll on 2016-11-30.
 */
public class TestUI extends JFrame{



    public TestUI() {
        super("dsf");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setVisible(false);

        showLoginUI();
    }

    private void showLoginUI() {
        new LoginUI(this);
    }

    public static void main(String[] args) {
        new TestUI();
    }
}
