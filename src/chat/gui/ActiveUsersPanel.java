package chat.gui;

import chat.gui.listenersinterfaces.ActiveUsersPanelListener;
import chat.model.ChatRoom;
import chat.model.Message;
import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ActiveUsersPanelListener activeUsersPanelListener;

    private String selection;
    private String username;

    private int getRow(Point point){
        return activeUsersPanel.locationToIndex(point);
    }

    void setUsername(String username){
        this.username = username;
    }

    void setSelection(String username){
        this.selection = username;
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
        JMenuItem invite =new JMenuItem("Invite");
        JMenuItem kick = new JMenuItem("Kick");
        JMenuItem whisper = new JMenuItem("Whisper");

        PopupActionListener listen = new PopupActionListener();

        invite.addActionListener(listen);
        kick.addActionListener(listen);
        whisper.addActionListener(listen);

        popupUserMenu.add(invite);
        popupUserMenu.add(kick);
        popupUserMenu.add(whisper);

        activeUsersPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                JList list = (JList)ev.getSource();
                if(ev.getClickCount() == 2){
                    int index = list.locationToIndex(ev.getPoint());
                    activeUsersPanel.setSelectedIndex(index);
                    String selected = (String) activeUsersPanel.getSelectedValue();
                    System.out.println(selected + "-- SELECTED USER");
                    activeUsersPanelListener.userInvitedOccured(selected);

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){

                    activeUsersPanel.setSelectedIndex(getRow(e.getPoint()));
                    String selected = (String) activeUsersPanel.getSelectedValue();
                    System.out.println(selected + "  " + username);
                    if(!selected.equals(username)) {
                        selection = selected;
                        popupUserMenu.show(activeUsersPanel, e.getX(), e.getY());
                    }
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

    class PopupActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            //System.out.println("Selected: " + actionEvent.getActionCommand());
            String event = actionEvent.getActionCommand();
            if(event.equals("Invite")){
                activeUsersPanelListener.userInvitedOccured(selection);
            } else if( event.equals("Whisper")){
                activeUsersPanelListener.whisperEventOccured(selection);
            }
        }
    }

    public void setListener(ActiveUsersPanelListener listener){
        this.activeUsersPanelListener = listener;
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

    public void actualizeAllUsers(ArrayList<String> users){
        activeUsersModel.removeAllElements();
        for (String user :
                users) {
            activeUsersModel.addElement(user);
        }
    }



}
