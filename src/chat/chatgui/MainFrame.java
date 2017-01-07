package chat.chatgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 07/01/2017.
 */
public class MainFrame  extends JFrame {

    private ChatsPanel chatsPanel;
    private UserPanel userPanel;
    private ActiveUsersPanel activeUsersPanel;
    private MessagePanel messagePanel;

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
        setJMenuBar(createMenuBar());

        /////////////////////////LAYOUT////////////////////

        setLayout(new BorderLayout());

        chatsPanel.setBorder(BorderFactory.createEmptyBorder(2,4,4,4));
        activeUsersPanel.setBorder(BorderFactory.createEmptyBorder(2,4,4,4));
        add(userPanel, BorderLayout.BEFORE_FIRST_LINE);
        add(chatsPanel, BorderLayout.WEST);
        add(messagePanel, BorderLayout.CENTER);
        add(activeUsersPanel, BorderLayout.EAST);

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
