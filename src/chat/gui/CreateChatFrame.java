package chat.gui;

import chat.gui.listenersinterfaces.CreateChatListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 14/01/2017.
 */
public class CreateChatFrame extends JFrame{

    private JButton createButton;
    private JButton cancelButton;
    private JTextField chatNameField;
    private JCheckBox privateChatBox;
    private JLabel chatNameLabel;
    private JLabel privateChatLabel;

    private CreateChatListener listener;

    public CreateChatFrame(){
        super("new Chat");

        createButton = new JButton("Create");
        cancelButton = new JButton("Cancel");
        chatNameLabel = new JLabel("Chatname: ");
        chatNameField = new JTextField(10);
        privateChatBox = new JCheckBox();
        privateChatLabel = new JLabel("Private? ");

        layoutComponents();

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chatName = chatNameField.getText();
                boolean isPrivate = privateChatBox.isSelected();
                CreateChatEvent ev = new CreateChatEvent(this, chatName, isPrivate);
                listener.chatCreated(ev);
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

    }

    public void setListener(CreateChatListener listener){
        this.listener = listener;
    }

    private void layoutComponents(){

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        add(chatNameLabel, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        add(chatNameField, gc);

        gc.gridy = 1;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        add(privateChatLabel, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        add(privateChatBox, gc);

        gc.gridy = 2;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        add(createButton, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        add(cancelButton, gc);

        Dimension dim = new Dimension(200, 200);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
