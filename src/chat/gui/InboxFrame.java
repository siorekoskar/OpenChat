package chat.gui;

import chat.gui.listenersinterfaces.InboxListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Oskar on 20/01/2017.
 */
public class InboxFrame extends JFrame {
    private JList messagesList;
    private DefaultListModel messagesModel;
    private JScrollPane messagesScrollPane;

    private InboxListener listener;

    InboxFrame(){
        super("Inbox");

        Dimension dim = new Dimension(350,300);
        Dimension dim2 = new Dimension(300,260);
        setMinimumSize(dim);
        setPreferredSize(dim);

        messagesModel = new DefaultListModel<String>();
        messagesList = new JList(messagesModel);
        messagesScrollPane = new JScrollPane(messagesList);
        messagesScrollPane.setPreferredSize(dim2);
        messagesScrollPane.setMinimumSize(dim2);

        setLocationRelativeTo(null);
        add(messagesScrollPane);
    }

    public void setInboxListener(InboxListener listener){
        this.listener = listener;
    }
    
    public void setMessagesList(List messages){
        messagesModel.removeAllElements();
        for (Object message :
                messages) {
            messagesModel.addElement(message);
        }
    }
}
