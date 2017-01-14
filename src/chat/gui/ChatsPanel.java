package chat.gui;

import chat.model.ChatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ChatsPanel extends JSplitPane {
    private JList publicChatsList;
    private JList privateChatsList;
    private JPopupMenu popupMenu;
    private JSplitPane splitPane;
    DefaultListModel chatPublicModel;
    DefaultListModel chatPrivateModel;

    private int getRow(Point point, JList list){
        return list.locationToIndex(point);
    }

    public ChatsPanel(){
        super(JSplitPane.VERTICAL_SPLIT);

        chatPublicModel = new DefaultListModel();
        chatPrivateModel = new DefaultListModel();
        publicChatsList = new JList(chatPublicModel);
        privateChatsList = new JList(chatPrivateModel);
        publicChatsList.setBorder(BorderFactory.createTitledBorder("Open chats:"));
        privateChatsList.setBorder(BorderFactory.createTitledBorder("Private chats:"));

        setResizeWeight(0.5);
        setOneTouchExpandable(true);

        ////////////////TEMP//////////////////
        popupMenu = new JPopupMenu();
        popupMenu.add(new JMenuItem("Join"));

        publicChatsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    publicChatsList.setSelectedIndex(getRow(e.getPoint(), publicChatsList));
                    popupMenu.show(publicChatsList, e.getX(), e.getY());
                }
            }
        });

        privateChatsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    privateChatsList.setSelectedIndex(getRow(e.getPoint(), privateChatsList));
                    popupMenu.show(privateChatsList, e.getX(), e.getY());
                }
            }
        });

        ////////////////TEMP//////////////////

        //////////////////////TEMP
        /*for (int i = 0; i < 40; i++) {
            chatPrivateModel.addElement("Private chat:" + i);
        }
        for (int i = 0; i < 15; i++) {
            chatPublicModel.addElement("Public chat:  " + i);
        }*/

        JScrollPane chatsPublicListScrollable = new JScrollPane(publicChatsList);
        JScrollPane chatsPrivateListScrollable = new JScrollPane(privateChatsList);

        Dimension chatDim = new Dimension(150, 100);
        chatsPublicListScrollable.setPreferredSize(chatDim);
        chatsPrivateListScrollable.setPreferredSize(chatDim);
        chatsPublicListScrollable.setMinimumSize(chatDim);
        chatsPrivateListScrollable.setMinimumSize(chatDim);

        add(chatsPublicListScrollable);
        add(chatsPrivateListScrollable);



    }

    public void fillChat(final Object objecList){
        List<String> stringList = new ArrayList<>();
        if(objecList instanceof java.util.List<?>){
            for(Object object: (java.util.List<?>) objecList){
                chatPublicModel.addElement(((ChatRoom)object).getChatName());
            }
        }


    }

    public void addChat(ChatRoom chat){
        for (int i = 0; i < chatPublicModel.size(); i++) {
            String str = (String)chatPublicModel.getElementAt(i);
            if(str.equals(chat.getChatName())) {
                System.out.println("Lipa");
                return;
            }
        }
        System.out.println("slabo");
        chatPublicModel.addElement(chat.getChatName());


    }
}
