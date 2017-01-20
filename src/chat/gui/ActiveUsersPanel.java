package chat.gui;

import chat.model.ChatRoom;
import chat.model.Message;
import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ActiveUsersPanel extends JSplitPane{
    private JList activeUsersPanel;
    private JList currentChatUsersPanel;
    private JPopupMenu popupUserMenu;
    private DefaultListModel activeUsersModel;
    private DefaultListModel currentChatUsersModel;

    private int getRow(Point point){
        return activeUsersPanel.locationToIndex(point);
    }

    public ActiveUsersPanel(){

        super(JSplitPane.VERTICAL_SPLIT);
        activeUsersModel = new DefaultListModel();
        activeUsersPanel = new JList(activeUsersModel);
        currentChatUsersModel = new DefaultListModel();
        currentChatUsersPanel = new JList(currentChatUsersModel);
        activeUsersPanel.setBorder(BorderFactory.createTitledBorder("Active users:"));
        currentChatUsersPanel.setBorder(BorderFactory.createTitledBorder("In chat users:"));

        setResizeWeight(0.5);
        setOneTouchExpandable(true);

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
        chatsListScrollable.setPreferredSize(new Dimension(150,100));

        JScrollPane currentChatScrollable = new JScrollPane(currentChatUsersPanel);
        currentChatScrollable.setPreferredSize(new Dimension(150,100));

       // setLayout(new BorderLayout());
        add(chatsListScrollable);
        add(currentChatScrollable);

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


    public void setUsersInChat(ArrayList<String> users){
        currentChatUsersModel.removeAllElements();
        try {
        for (String user :
                users) {

                currentChatUsersModel.addElement(user);

        }}catch(NullPointerException e){
            System.out.println( "exception");
        }
    }


}
