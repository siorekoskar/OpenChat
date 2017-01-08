package chat.chatgui;

import chat.chatgui.listenersinterfaces.UserPanelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 07/01/2017.
 */
public class UserPanel extends JPanel implements ActionListener{

    private JButton logoutButton;
    private JButton chatManagerButton;
    private JButton inboxButton;

    private UserPanelListener listener;

    public UserPanel() {

        chatManagerButton = new JButton("Chat Manager");
        inboxButton = new JButton("Inbox");
        logoutButton = new JButton("Logout");

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(inboxButton);
        add(chatManagerButton);
        add(logoutButton);

        ////////////////LISTENERS/////////////////
        logoutButton.addActionListener(this);
        chatManagerButton.addActionListener(this);
        inboxButton.addActionListener(this);
    }

    public void setUserPanelListener(UserPanelListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (listener != null) {
            if (clicked == logoutButton) {
                listener.logoutEventOccured();
            } else if (clicked == chatManagerButton){
                listener.chatboxEventOccured();
            } else if (clicked == inboxButton) {
                listener.inboxEventOccured();
            }
        }
    }

}