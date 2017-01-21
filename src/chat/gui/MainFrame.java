package chat.gui;

import chat.controller.ClientController;
import chat.controller.DbController;
import chat.gui.listenersinterfaces.*;
import chat.model.ChatRoom;
import chat.model.Message;
import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.List;

/**
 * Created by Oskar on 07/01/2017.
 */
public class MainFrame extends JFrame {

    private ChatsPanel chatsPanel;
    private UserPanel userPanel;
    private ActiveUsersPanel activeUsersPanel;
    private MessagePanel messagePanel;
    private LoginDialog loginDialog;
    private CreateChatFrame frame;
    private InboxFrame inboxFrame;
    private PrivateMessageFrame privateMessageFrame;

    private String username;

    private ClientController clientController;

    private static int port;
    private static String host;

    private boolean connected = false;


    public MainFrame(String host, int port) {
        super("Chat");

        this.host = host;
        this.port = port;

        setSize(800, 600);
        setMinimumSize(new Dimension(400, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        setLocationRelativeTo(null);

        //////////////create swing component/////////////////////
        chatsPanel = new ChatsPanel();
        userPanel = new UserPanel();
        activeUsersPanel = new ActiveUsersPanel();
        messagePanel = new MessagePanel();
        loginDialog = new LoginDialog(this, host, port);
        inboxFrame = new InboxFrame();
        privateMessageFrame = new PrivateMessageFrame();

        clientController = new ClientController(host, port, MainFrame.this);

        setJMenuBar(createMenuBar());
        this.setVisible(false);

        //////////////////////LISTENERS/////////////////////

        chatsPanel.setChatsPanelListener(new ChatsPanelListener() {
            @Override
            public void wentToChatOccured(String msg) {
                Message message = new Message(Message.CONNECTTOCHAT, username, msg);

                clientController.sendMessage(message);


            }
        });

        userPanel.setUserPanelListener(new UserPanelListener() {
            @Override
            public void logoutEventOccured() {
                MainFrame.this.setVisible(false);
                clientController.disconnect();
                loginDialog.setVisible(true);

                System.out.println("LOGOUT CLICKED");

            }

            @Override
            public void chatboxEventOccured() {
                frame = new CreateChatFrame();
                frame.setListener(new CreateChatListener() {
                    @Override
                    public void chatCreated(CreateChatEvent e) {
                        e.setAdmin(username);
                        clientController.newChatCreated(e);

                    }
                });
            }

            @Override
            public void inboxEventOccured() {
                inboxFrame.setVisible(true);
            }

            @Override
            public void refreshEventOccured() {

            }
        });

        inboxFrame.setInboxListener(new InboxListener() {
            @Override
            public void respondOccured(String msg) {
                privateMessageFrame.setVisible(true);
                System.out.println(msg);
            }
        });

        activeUsersPanel.setListener(new ActiveUsersPanelListener() {
            @Override
            public void userInvitedOccured(String selected) {
                clientController.userInvited(selected, username);
            }

            @Override
            public void whisperEventOccured(String username) {
                privateMessageFrame.setMessageTo(username);
                privateMessageFrame.setVisible(true);
            }
        });

        messagePanel.setMessageListener(new MessageListener() {
            @Override
            public void messageSent(String msg) {
                clientController.sendMessage(new Message(Message.MESSAGE, username, msg));

            }

            @Override
            public void fileSent() {
                System.out.println("fileSent");
            }
        });

        loginDialog.setFormListener(new FormListener() {

            @Override
            public void loginEventOccured(FormEvent e) {

                clientController.sendMessage(new Message(Message.LOGINMSG, e.getLogin(), ""));
            }

            @Override
            public void registeredEventOccured(FormEvent e) {

                clientController.sendMessage(new Message(Message.REGISTER, e.getLogin(),e.getPass(), ""));
            }
        });

        privateMessageFrame.setListener(new PrivateMessageListener() {
            @Override
            public void privateMessageOccured(String msg, String messageTo) {
                clientController.sendPrivateMessage(msg, messageTo, username);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientController.disconnect();
                dispose();
                System.gc();
                System.exit(0);
            }
        });


        /////////////////////////LAYOUT////////////////////

        setLayout(new BorderLayout());

        chatsPanel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        activeUsersPanel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        add(userPanel, BorderLayout.BEFORE_FIRST_LINE);
        add(chatsPanel, BorderLayout.WEST);
        add(messagePanel, BorderLayout.CENTER);
        add(activeUsersPanel, BorderLayout.EAST);

    }

    public void actualizeAllUsers(ArrayList<String> arr){
        activeUsersPanel.actualizeAllUsers(arr);
    }

    public void sendMsg(Message msg) {
        messagePanel.append(msg);
    }

    public void sendRegistered(Message msg){
        JOptionPane.showMessageDialog(MainFrame.this, "Registered",
                "Your username: " + msg.getUser(), JOptionPane.INFORMATION_MESSAGE);
    }

    public void sendDissallowed(Message msg) {
        JOptionPane.showMessageDialog(MainFrame.this, "Wrong username/password",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void sendExists(Message msg){
        JOptionPane.showMessageDialog(MainFrame.this, "User already exists",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void sendAllowed(Message msg) {
        loginDialog.setVisible(false);
        JOptionPane.showMessageDialog(MainFrame.this, "Logged",
                "Succes", JOptionPane.INFORMATION_MESSAGE);
        username = msg.getUser();

        MainFrame.this.setTitle(username);
        MainFrame.this.setVisible(true);
        activeUsersPanel.setUsername(username);
        activeUsersPanel.setSelection(username);
    }

    public void sendNotAllowed(){
        JOptionPane.showMessageDialog(MainFrame.this, "You are not allowed to join",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void notConnectedDialog(){
        JOptionPane.showMessageDialog(MainFrame.this, "Connection not found",
                "Error", JOptionPane.ERROR_MESSAGE);
        Launcher launcher = new Launcher();
        dispose();
        System.gc();
    }

    public void connectedDialog(){
        JOptionPane.showMessageDialog(MainFrame.this, "Connected",
                "Succes", JOptionPane.INFORMATION_MESSAGE);
        loginDialog.setVisible(true);

    }

    public void sendUsersOfChat(ArrayList<String> users){
        activeUsersPanel.setUsersInChat(users);
    }

    public void sendInboxMessages(List messages){
        inboxFrame.setMessagesList(messages);
    }

    public void sendLeft(Message msg){
        activeUsersPanel.setUsersInChat(msg.getUsersIn());
    }

    public void sendUserLeft(Message msg){
        activeUsersPanel.actualizeAllUsers(msg.getUsersIn());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("User Panel");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window");
        JMenu showMenu = new JMenu("Show");

        JCheckBoxMenuItem showUsersItem = new JCheckBoxMenuItem("Users");
        showUsersItem.setSelected(true);
        JCheckBoxMenuItem showChatsItem = new JCheckBoxMenuItem("Chats");
        showChatsItem.setSelected(true);
        JCheckBoxMenuItem showUserPanelItem = new JCheckBoxMenuItem("User Panel");
        showUserPanelItem.setSelected(true);

        showMenu.add(showChatsItem);
        showMenu.add(showUsersItem);
        showMenu.add(showUserPanelItem);
        windowMenu.add(showMenu);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        showChatsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ev.getSource();
                chatsPanel.setVisible(menuItem.isSelected());
            }
        });

        showUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ev.getSource();
                activeUsersPanel.setVisible(menuItem.isSelected());
            }
        });

        showUserPanelItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ev.getSource();
                userPanel.setVisible(menuItem.isSelected());
            }
        });

        return menuBar;
    }

    public void sendChat(ChatRoom chat) {
        chatsPanel.addChat(chat);
    }

    public void sendUser(User user) {
        activeUsersPanel.addUser(user);
    }

    public String getHost(){
        return host;
    }

    public int getPort(){
        return  port;
    }
}
