package chat.gui;

import chat.controller.ClientControllerInterface;
import chat.controller.ClientControllerFactory;
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
import java.util.List;

/**
 * Created by Oskar on 07/01/2017.
 */
public class MainFrame extends JFrame implements GuiInterface{

    private ChatsPanel chatsPanel;
    private UserPanel userPanel;
    private ActiveUsersPanel activeUsersPanel;
    private MessagePanel messagePanel;
    private LoginDialog loginDialog;
    private CreateChatFrame frame;
    private InboxFrame inboxFrame;
    private UsersFrame usersFrame;
    private PrivateMessageFrame privateMessageFrame;
    private YourChatsFrame yourChatsFrame;

    private String username;

    private ClientControllerInterface clientController;

    private int port;
    private String host;

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
        usersFrame = new UsersFrame("Users");
        yourChatsFrame = new YourChatsFrame();

        //clientController = new ClientController(host, port, MainFrame.this);
        clientController = ClientControllerFactory.returnController(host, port, MainFrame.this,
                ClientControllerFactory.CLIENTCONTROLLER);

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

        yourChatsFrame.setListener(new YourChatsFrameListener() {
            @Override
            public void invited(String invited, String chat) {
                clientController.userInvited(invited, chat, username);
                //System.out.println("invited user " + invited + " to chat " + chat + " by "+ username);
                yourChatsFrame.setVisible(false);
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
            public void usersEventOccured() {
                usersFrame.setVisible(true);
            }
        });

        inboxFrame.setInboxListener(new InboxListener() {
            @Override
            public void respondOccured(String msg) {
                privateMessageFrame.setVisible(true);
                privateMessageFrame.setMessageTo(msg);
            }
        });

        activeUsersPanel.setListener(new ActiveUsersPanelListener() {
            @Override
            public void userInvitedOccured(String selected) {
                //
                yourChatsFrame.setInvited(selected);
                yourChatsFrame.setVisible(true);

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

        usersFrame.setListener(new UsersFrameListener() {
            @Override
            public void whisperEventOccured(String to) {
                privateMessageFrame.setMessageTo(to);
                privateMessageFrame.setVisible(true);
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

    public void popChatAlreadyExistsDialog(String chatname){
        JOptionPane.showMessageDialog(MainFrame.this, "Chat " + chatname + " already exists",
                "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    public void sendMsg(Message msg) {
        messagePanel.append(msg);
    }

    public void popFail(String msg){
        JOptionPane.showMessageDialog(MainFrame.this, msg,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void popSuccess(String msg){
        JOptionPane.showMessageDialog(MainFrame.this, msg,
                "Succes", JOptionPane.INFORMATION_MESSAGE);
    }

    public void popLoggedDialog(String user) {
        loginDialog.setVisible(false);
        JOptionPane.showMessageDialog(MainFrame.this, "Logged",
                "Succes", JOptionPane.INFORMATION_MESSAGE);
        username = user;

        MainFrame.this.setTitle(username);
        MainFrame.this.setVisible(true);
        activeUsersPanel.setUsername(username);
        activeUsersPanel.setSelection(username);
        usersFrame.setUsername(username);
        messagePanel.setUsername(username);
    }

    public void popNotConnectedDialog(String msg){
        JOptionPane.showMessageDialog(MainFrame.this, msg,
                "Error", JOptionPane.ERROR_MESSAGE);
        Launcher launcher = new Launcher();
        dispose();
        System.gc();
    }

    public void popConnectedDialog(){
        JOptionPane.showMessageDialog(MainFrame.this, "Connected",
                "Succes", JOptionPane.INFORMATION_MESSAGE);
        loginDialog.setVisible(true);

    }

    public void setUsersInChat(List users){
        activeUsersPanel.setUsersInChat(users);
    }

    public void sendInboxMessages(List messages){
        inboxFrame.setMessagesList(messages);
    }

    public void userLeftChat(List users){
        activeUsersPanel.setUsersInChat(users);
    }

    public void actualizeCauseUserLeftServer(List users){
        activeUsersPanel.actualizeAllUsers(users);
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

    public void addChatToPanel(ChatRoom chat) {
        chatsPanel.addChat(chat);
    }

    public void addUserWhoJoinedServer(User user) {
        activeUsersPanel.addUser(user);
    }

    public void sendUsersRegisteredList(List list){
        usersFrame.setUsersRegisteredList(list);
    }

    public void sendPrivateMsgToGui(String msg){
        inboxFrame.addPrivateMessage(msg);
        userPanel.notifyUser("New message in inbox!");
    }

    public void setYourChatRooms(List chatRooms){
        yourChatsFrame.setChatList(chatRooms);
    }

    public void actualizeChats(List chats, List privateList){
        chatsPanel.setChats(chats, privateList);
    }


}
