package chat.gui;

import chat.controller.ClientController;
import chat.controller.DbController;
import chat.gui.listenersinterfaces.*;
import chat.model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.prefs.Preferences;

/**
 * Created by Oskar on 07/01/2017.
 */
public class MainFrame  extends JFrame {

    private ChatsPanel chatsPanel;
    private UserPanel userPanel;
    private ActiveUsersPanel activeUsersPanel;
    private MessagePanel messagePanel;
    private Preferences preferenceLogin;
    private LoginDialog loginDialog;
    private CreateChatFrame frame;

    private String username;

    private DbController dbController;
    private ClientController clientController;

    private static final int port = 3308;
    private static final String host = "0.0.0.0";


    public MainFrame() {
        super("Chat");

        setSize(800, 600);
        setMinimumSize(new Dimension(400,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        //////////////create swing component/////////////////////
        chatsPanel = new ChatsPanel();
        userPanel = new UserPanel();
        activeUsersPanel = new ActiveUsersPanel();
        messagePanel = new MessagePanel();
        loginDialog = new LoginDialog(this);

        dbController = new DbController();

        setJMenuBar(createMenuBar());
        this.setVisible(false);

        //////////////////////LISTENERS/////////////////////
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

        loginDialog.setFormListener(new FormListener(){

            @Override
            public void loginEventOccured(FormEvent e) {
                System.out.println(e.getLogin());
                try{
                    dbController.connect();
                    dbController.load();
                    if(dbController.checkIfUserExists(e)) {

                        loginDialog.setVisible(false);
                        username = e.getLogin();
                        MainFrame.this.setTitle(username);
                        MainFrame.this.setVisible(true);
                        JOptionPane.showMessageDialog(MainFrame.this, "Logged",
                                "Succes", JOptionPane.ERROR_MESSAGE);

                        clientController = new ClientController(host, port, MainFrame.this, username);

                    }
                    else{
                        JOptionPane.showMessageDialog(MainFrame.this, "Error",
                                "Bad data", JOptionPane.ERROR_MESSAGE);
                    }

                } catch(Exception e5){

                }

            }

            @Override
            public void registeredEventOccured(FormEvent e) {
                System.out.println(e.getLogin());
                System.out.println(e.getPass());

                try {
                    dbController.connect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                try {
                   // dbController.checkIfUserExists(e);
                    dbController.load();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try{
                if(dbController.checkIfUserExists(e)) {
                    JOptionPane.showMessageDialog(MainFrame.this, "User already exists",
                            "Error", JOptionPane.ERROR_MESSAGE);

                } else {
                    dbController.addUser(e);
                    dbController.save();
                    JOptionPane.showMessageDialog(MainFrame.this, "Registered",
                            "Your username: "+ e.getLogin(), JOptionPane.OK_OPTION);
                }
                } catch(SQLException e3){
                    JOptionPane.showMessageDialog(MainFrame.this, "Unable to save to database",
                            "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        /////////////////////////LAYOUT////////////////////

        setLayout(new BorderLayout());

        chatsPanel.setBorder(BorderFactory.createEmptyBorder(2,4,4,4));
        activeUsersPanel.setBorder(BorderFactory.createEmptyBorder(2,4,4,4));
        add(userPanel, BorderLayout.BEFORE_FIRST_LINE);
        add(chatsPanel, BorderLayout.WEST);
        add(messagePanel, BorderLayout.CENTER);
        add(activeUsersPanel, BorderLayout.EAST);
        loginDialog.setVisible(true);


    }

    public void sendMsg(String msg){
        messagePanel.append(msg);
    }

    private JMenuBar createMenuBar(){
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
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)ev.getSource();
                chatsPanel.setVisible(menuItem.isSelected());
            }
        });

        showUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)ev.getSource();
                activeUsersPanel.setVisible(menuItem.isSelected());
            }
        });

        showUserPanelItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)ev.getSource();
                userPanel.setVisible(menuItem.isSelected());
            }
        });

        return menuBar;
    }
}
