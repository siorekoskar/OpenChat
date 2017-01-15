package chat.gui;

import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ActiveUsersPanel extends JPanel {
    private JList activeUsersPanel;
    private JPopupMenu popupUserMenu;
    private DefaultListModel activeUsersModel;

    private int getRow(Point point){
        return activeUsersPanel.locationToIndex(point);
    }

    public ActiveUsersPanel(){

        activeUsersModel = new DefaultListModel();
        activeUsersPanel = new JList(activeUsersModel);
        activeUsersPanel.setBorder(BorderFactory.createTitledBorder("Active users:"));


        ////////////////TEMP//////////////////
        popupUserMenu = new JPopupMenu();
        popupUserMenu.add(new JMenuItem("Invite"));
        popupUserMenu.add(new JMenuItem("Kick"));
        popupUserMenu.add(new JMenuItem("Whisper"));

        activeUsersPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    activeUsersPanel.setSelectedIndex(getRow(e.getPoint()));
                    popupUserMenu.show(activeUsersPanel, e.getX(), e.getY());
                }
            }
        });
        ////////////////TEMP//////////////////


        JScrollPane chatsListScrollable = new JScrollPane(activeUsersPanel);
        chatsListScrollable.setPreferredSize(new Dimension(120,0));

        setLayout(new BorderLayout());
        add(chatsListScrollable);

    }

    public void addUser(User user){
        String userName = user.getLogin();
        for (int i = 0; i < activeUsersModel.size(); i++) {
            String userOnList = (String)activeUsersModel.getElementAt(i);
            if(userOnList.equals(userName)){
                return;
            }
        }
        activeUsersModel.addElement(userName);
    }
}
