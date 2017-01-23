package main.java.pl.edu.agh.kis.openchat.gui;

import main.java.pl.edu.agh.kis.openchat.gui.listenersinterfaces.YourChatsFrameListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Oskar on 21/01/2017.
 */
public class YourChatsFrame extends JFrame {
    private JList chatsList;
    private DefaultListModel chatsModel;
    private JScrollPane chatsScrollPane;
    private JPopupMenu popupMenu;
    private String selected;
    private String invited;
    private JButton inviteButton;
    private JButton cancelButton;
    private JLabel inviteLabel;

    private YourChatsFrameListener listener;

    YourChatsFrame() {
        super("Your chats");
        Dimension dim = new Dimension(350, 300);
        Dimension dim2 = new Dimension(300, 260);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setVisible(false);

        chatsModel = new DefaultListModel<String>();
        chatsList = new JList(chatsModel);
        chatsScrollPane = new JScrollPane(chatsList);
        chatsScrollPane.setPreferredSize(dim2);
        chatsScrollPane.setMinimumSize(dim2);
        inviteLabel = new JLabel();
        inviteButton = new JButton("Invite");
        cancelButton = new JButton("Cancel");
        chatsList.setMinimumSize(new Dimension(200,190));
        chatsList.setMinimumSize(new Dimension(250,190));

        setLocationRelativeTo(null);

        layoutComponents();

        chatsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (SwingUtilities.isLeftMouseButton(ev)) {
                    chatsList.setSelectedIndex(getRow(ev.getPoint()));
                    if (ev.getClickCount() == 2) {
                        chatsList.setSelectedIndex(getRow(ev.getPoint()));
                        selected = (String) chatsList.getSelectedValue();
                        listener.invited(invited, selected);
                    }
                }
            }
        });
    }

    void layoutComponents() {
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        setLayout(new GridBagLayout());

        int space = 8;
        Border titledBorder = BorderFactory.createTitledBorder("Your chats:");
        Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titledBorder));

        controlsPanel.setMinimumSize(new Dimension(300,190));
        controlsPanel.setPreferredSize(new Dimension(300,190));


        GridBagConstraints gc = new GridBagConstraints();
        //controlsPanel.add(chatsScrollPane);

        gc.gridy = 0;
        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 0.5;
        gc.fill=GridBagConstraints.NONE;
        add(inviteLabel, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 2;
        gc.fill = GridBagConstraints.NONE;
        //add(controlsPanel,gc);
        add(chatsScrollPane, gc);
       /* gc.gridx = 0;
        gc.gridy++;
        gc.weighty = 1;
        controlsPanel.add(chatsScrollPane);*/

        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(inviteButton, BorderLayout.WEST);
        buttonsPanel.add(cancelButton, BorderLayout.EAST);


        gc.gridx = 0;
        gc.gridy++;
        gc.weighty = 1;
        add(buttonsPanel,gc);

    }

    public void setChatList(java.util.List messages) {
        chatsModel.removeAllElements();
        System.out.println(messages);
        for (Object message :
                messages) {
            chatsModel.addElement(message);
        }
    }

    void addChat(String msg) {
        chatsModel.addElement(msg);
    }

    private int getRow(Point point) {
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
        inviteLabel.setText("Invite user: " + invited);
    }


}
