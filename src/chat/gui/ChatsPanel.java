package chat.gui;

import chat.gui.listenersinterfaces.ChatsPanelListener;
import chat.model.ChatRoom;
import chat.model.Message;

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

    public void setChatsPanelListener(ChatsPanelListener chatsPanelListener) {
        this.chatsPanelListener = chatsPanelListener;
    }

    private ChatsPanelListener chatsPanelListener;

    private int getRow(Point point, JList list) {
        return list.locationToIndex(point);
    }

    public ChatsPanel() {
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
            public void mouseClicked(MouseEvent ev){
                JList list = (JList)ev.getSource();
                if(ev.getClickCount() == 2){
                    int index = list.locationToIndex(ev.getPoint());
                    publicChatsList.setSelectedIndex(index);
                    String selected = (String) publicChatsList.getSelectedValue();
                    System.out.println(selected + "-- SELECTED CHAT");
                    chatsPanelListener.wentToChatOccured(selected);

                }
            }

        });

        privateChatsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    privateChatsList.setSelectedIndex(getRow(e.getPoint(), privateChatsList));
                    popupMenu.show(privateChatsList, e.getX(), e.getY());
                }

            }

            @Override
            public void mouseClicked(MouseEvent ev){
                JList list = (JList)ev.getSource();
                if(ev.getClickCount() == 2){
                    int index = list.locationToIndex(ev.getPoint());
                    privateChatsList.setSelectedIndex(index);
                    String selected = (String) privateChatsList.getSelectedValue();
                    System.out.println(selected + "-- SELECTED CHAT");
                    chatsPanelListener.wentToChatOccured(selected);

                }
            }
        });

        // popupMenu.listener


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

    public void addChat(ChatRoom chat) {
        if (!chat.isPrivate()) {
            addChatToList(chat, chatPublicModel);
        } else {
            addChatToList(chat, chatPrivateModel);
        }
    }

    public void addChatToList(ChatRoom chat, DefaultListModel dlm) {
        String chatName = chat.getChatName();
        for (int i = 0; i < dlm.size(); i++) {
            String str = (String) dlm.getElementAt(i);
            if (str.equals(chatName)) {
                return;
            }
        }
        dlm.addElement(chatName);


    }

    void setChats(List chats, List privateList){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatPublicModel.removeAllElements();
                chatPrivateModel.removeAllElements();

                for (int i = 0; i < chats.size(); i++) {
                    String chat = (String) chats.get(i);
                    Boolean isPrivate = (Boolean) privateList.get(i);

                    if(isPrivate){
                        chatPrivateModel.addElement(chat);
                    } else {
                        chatPublicModel.addElement(chat);
                    }
                }
            }
        });

    }
}
