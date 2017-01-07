package chat.chatgui;

import javax.swing.*;
import java.awt.*;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        //////////////create swing component/////////////////////
        chatsPanel = new ChatsPanel();
        userPanel = new UserPanel();
        activeUsersPanel = new ActiveUsersPanel();
        messagePanel = new MessagePanel();

        /////////////////////////LAYOUT////////////////////

        setLayout(new BorderLayout());
        userPanel.setBorder(BorderFactory.createEtchedBorder());
        chatsPanel.setBorder(BorderFactory.createEtchedBorder());
        messagePanel.setBorder(BorderFactory.createEtchedBorder());
        activeUsersPanel.setBorder(BorderFactory.createEtchedBorder());
        add(userPanel, BorderLayout.BEFORE_FIRST_LINE);
        add(chatsPanel, BorderLayout.WEST);
        add(messagePanel, BorderLayout.CENTER);
        add(activeUsersPanel, BorderLayout.EAST);

        //setLayout(new GridBagLayout());
       /* GridBagConstraints gc = new GridBagConstraints();

        /////////////////////////UserPanel////////////////////
        gc.weightx = 3;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(2,2,2,2);
        userPanel.setBorder(BorderFactory.createEtchedBorder());
        add(userPanel, gc);*/

        /////////////////////////ChatsActivePanel////////////////
        /*gc.weightx = 0.6;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 1;

        //chatsPanel.setBorder(BorderFactory.createEtchedBorder());
        gc.fill = GridBagConstraints.NONE;
        add(chatsPanel);*/


        /////////////////////////MessagePanel////////////////////
        /*gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 1;

        messagePanel.setBorder(BorderFactory.createEtchedBorder());
        gc.fill = GridBagConstraints.NONE;
        add(messagePanel, gc);*/

        /////////////////////////ActiveUsersPanel////////////////
      /*  gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 2;
        gc.gridy = 1;

        gc.fill = GridBagConstraints.NONE;
        activeUsersPanel.setBorder(BorderFactory.createEtchedBorder());
        add(activeUsersPanel);*/

    }
}
