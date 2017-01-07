package chat.chatgui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ChatsPanel extends JPanel {
    private JList publicChatsList;
    private JList privateChatsList;

    public ChatsPanel(){

        DefaultListModel chatPublicModel = new DefaultListModel();
        DefaultListModel chatPrivateModel = new DefaultListModel();
        publicChatsList = new JList(chatPublicModel);
        privateChatsList = new JList(chatPrivateModel);
        publicChatsList.setBorder(BorderFactory.createTitledBorder("Open chats:"));
        privateChatsList.setBorder(BorderFactory.createTitledBorder("Private chats:"));

        //////////////////////TEMP
        for (int i = 0; i < 40; i++) {
            chatPrivateModel.addElement("Private chat:" + i);
        }
        for (int i = 0; i < 2; i++) {
            chatPublicModel.addElement("Public chat:  " + i);
        }

        JScrollPane chatsPublicListScrollable = new JScrollPane(publicChatsList);
        JScrollPane chatsPrivateListScrollable = new JScrollPane(privateChatsList);

        Dimension chatDim = new Dimension(150,0);
        chatsPublicListScrollable.setPreferredSize(chatDim);
        chatsPrivateListScrollable.setPreferredSize(chatDim);

        //setLayout(new BorderLayout());
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        ////////////////FIRST ROW///////////////

        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;

        gc.fill = GridBagConstraints.BOTH;
        add(chatsPublicListScrollable, gc);

        gc.insets = new Insets(4,0,0,0);
        gc.gridy = 1;
        add(chatsPrivateListScrollable, gc);

    }
}
