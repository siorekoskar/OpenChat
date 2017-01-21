package chat.gui;

import chat.gui.listenersinterfaces.UsersFrameListener;
import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Oskar on 21/01/2017.
 */
public class UsersFrame extends JFrame{

    private JList usersList;
    private DefaultListModel usersModel;
    private String selected;
    private JPopupMenu popupMenu;
    private JScrollPane scrollPane;
    private UsersFrameListener listener;
    private String username;

    UsersFrame(String users){
        super(users);
        Dimension dim = new Dimension(150,300);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setLocationRelativeTo(null);

        usersModel = new DefaultListModel<String>();
        usersList = new JList(usersModel);
        scrollPane = new JScrollPane(usersList);
        popupMenu = new JPopupMenu();
        JMenuItem whisperItem = new JMenuItem("Whisper");
        popupMenu.add(whisperItem);

        scrollPane.setMinimumSize(dim);
        add(scrollPane);

        for (int i = 0; i < 10; i++) {
            usersModel.addElement("SS");
        }

        PopupActionListener listen = new PopupActionListener();
        whisperItem.addActionListener(listen);

        usersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (SwingUtilities.isRightMouseButton(ev)){
                    usersList.setSelectedIndex(getRow(ev.getPoint()));
                    selected = (String) usersList.getSelectedValue();
                    if (!selected.equals(username)) {
                        popupMenu.show(usersList, ev.getX(), ev.getY());
                    }
                }
            }
        });
    }

    private int getRow(Point point){
        return usersList.locationToIndex(point);
    }

    private class PopupActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String event = actionEvent.getActionCommand();
            if(event.equals("Whisper")){
                listener.whisperEventOccured(selected);
                System.out.println("whisper to: " + selected);
            }
        }
    }


    public void setListener(UsersFrameListener listener) {
        this.listener = listener;
    }

    public void setUsersRegisteredList(List list){
        usersModel.removeAllElements();
        for (Object obj:
             list) {
            String user = (String)obj;
            usersModel.addElement(user);
        }
    }

    void setUsername(String username){
        this.username = username;
    }
}
