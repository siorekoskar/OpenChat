package chat.chatgui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Oskar on 07/01/2017.
 */
public class UserPanel extends JPanel {


    private JButton logoutButton;
    private JButton chatManagerButton;
    private JButton inboxButton;


    public UserPanel() {
        /*temporaryUserButton = new JButton("Temporary name");
        loginButton = new JButton("Login");
        RegisterButton = new JButton("Register");*/

        chatManagerButton = new JButton("Chat Manager");
        inboxButton = new JButton("Inbox");
        logoutButton = new JButton("Logout");

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(inboxButton);
        add(chatManagerButton);
        add(logoutButton);

    }

    /*private JLabel setUsernameLabel;
    private JLabel setPasswordLabel;
    private JButton setUsernameButton;
    private JButton registerUserButton;
    private JButton createChatButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public UserPanel() {

        setUsernameLabel = new JLabel("Set username:");
        setPasswordLabel = new JLabel("Set password:");
        setUsernameButton = new JButton("Set");
        registerUserButton = new JButton("Register");
        createChatButton = new JButton("New Chat");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(setUsernameLabel);
        add(usernameField);
        add(setUsernameButton);
        add(setPasswordLabel);
        add(passwordField);
        add(registerUserButton);
        add(createChatButton);

    }*/
}
