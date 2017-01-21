package chat.gui;

import chat.gui.listenersinterfaces.InboxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Oskar on 20/01/2017.
 */
public class InboxFrame extends JFrame {
    private JList messagesList;
    private DefaultListModel messagesModel;
    private JScrollPane messagesScrollPane;
    private JPopupMenu popupMenu;
    private String selected;

    private InboxListener listener;

    InboxFrame(){
        super("Inbox");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = new Dimension(350,300);
        Dimension dim2 = new Dimension(300,260);
        setMinimumSize(dim);
        setPreferredSize(dim);

        messagesModel = new DefaultListModel<String>();
        messagesList = new JList(messagesModel);
        messagesScrollPane = new JScrollPane(messagesList);
        messagesScrollPane.setPreferredSize(dim2);
        messagesScrollPane.setMinimumSize(dim2);

        messagesModel.addElement("TEST");

        popupMenu = new JPopupMenu();
        JMenuItem respondItem = new JMenuItem("Respond");
        popupMenu.add(respondItem);

        PopupActionListener listen = new PopupActionListener();
        respondItem.addActionListener(listen);

        setLocationRelativeTo(null);
        add(messagesScrollPane);

        messagesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (SwingUtilities.isRightMouseButton(ev)){
                    System.out.println("SELECT");

                    messagesList.setSelectedIndex(getRow(ev.getPoint()));
                    selected = (String) messagesList.getSelectedValue();
                    popupMenu.show(messagesList, ev.getX(), ev.getY());

                }
            }
        });
    }

    private int getRow(Point point){
        return messagesList.locationToIndex(point);
    }


    private class PopupActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String event = actionEvent.getActionCommand();
            if(event.equals("Respond")){
                listener.respondOccured(selected);
            }
        }
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
