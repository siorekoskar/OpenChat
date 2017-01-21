package chat.gui;

import chat.gui.listenersinterfaces.YourChatsFrameListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by Oskar on 21/01/2017.
 */
public class YourChatsFrame extends JFrame{
    private JList chatsList;
    private DefaultListModel chatsModel;
    private JScrollPane chatsScrollPane;
    private JPopupMenu popupMenu;
    private String selected;
    private String invited;

    private YourChatsFrameListener listener;

    YourChatsFrame(){
        super("Your chats");
        Dimension dim = new Dimension(350,300);
        Dimension dim2 = new Dimension(300,260);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setVisible(false);

        chatsModel = new DefaultListModel<String>();
        chatsList = new JList(chatsModel);
        chatsScrollPane = new JScrollPane(chatsList);
        chatsScrollPane.setPreferredSize(dim2);
        chatsScrollPane.setMinimumSize(dim2);

        setLocationRelativeTo(null);
        add(chatsScrollPane);

        chatsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (SwingUtilities.isLeftMouseButton(ev)){
                    chatsList.setSelectedIndex(getRow(ev.getPoint()));
                    if(ev.getClickCount() == 2) {
                        chatsList.setSelectedIndex(getRow(ev.getPoint()));
                        selected = (String) chatsList.getSelectedValue();
                        listener.invited(invited, selected);
                    }
                }
            }
        });
    }

    public void setChatList(java.util.List messages){
        chatsModel.removeAllElements();
        for (Object message :
                messages) {
            chatsModel.addElement(message);
        }
    }

    void addChat(String msg){
        chatsModel.addElement(msg);
    }

    private int getRow(Point point){
        return chatsList.locationToIndex(point);
    }

    public void setListener(YourChatsFrameListener listener) {
        this.listener = listener;
    }

    public String getInvited() {
        return invited;
    }

    public void setInvited(String invited) {
        this.invited = invited;
    }

}
