package chat.chatgui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ActiveUsersPanel extends JPanel {
    private JList activeUsersPanel;

    public ActiveUsersPanel(){

        DefaultListModel activeUsersModel = new DefaultListModel();
        activeUsersPanel = new JList(activeUsersModel);
        activeUsersPanel.setBorder(BorderFactory.createTitledBorder("Active users:"));

        for (int i = 0; i < 40; i++) {
            activeUsersModel.addElement("User nr  " + i);
        }

        JScrollPane chatsListScrollable = new JScrollPane(activeUsersPanel);
        chatsListScrollable.setPreferredSize(new Dimension(120,0));

        setLayout(new BorderLayout());
        add(chatsListScrollable);

    }
}
