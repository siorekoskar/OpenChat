package chat.chatgui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Oskar on 07/01/2017.
 */
public class MessagePanel extends JPanel {

    private JButton sendMessageButton;
    private JButton sendFileButton;
    private JTextArea newMessageArea;
    private JTextArea messagesArea;

    MessagePanel(){
        newMessageArea = new JTextArea();
        messagesArea = new JTextArea();
        sendFileButton = new JButton("Upload File");
        sendMessageButton = new JButton("Send");

        messagesArea.setEditable(false);
        messagesArea.setBorder(BorderFactory.createTitledBorder("CHATNAME"));
        newMessageArea.setBorder(BorderFactory.createTitledBorder("Message"));
        Dimension buttonDim = new Dimension(100, 40);
        sendFileButton.setPreferredSize(buttonDim);
        sendMessageButton.setPreferredSize(buttonDim);

        add(messagesArea);

        layoutComponents();


    }

    private void layoutComponents(){

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        ////////////////MESSAGES//////////////////

        gc.gridx = 0;
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 2;

        gc.fill = GridBagConstraints.BOTH;

        gc.insets = new Insets(2,2,2,2);
        add(new JScrollPane(messagesArea), gc);

        //////////////SEND MESSAGE//////////////

        gc.weightx = 1;
        gc.weighty = 0.3;

        gc.gridx = 0;
        gc.gridy = 1;

        gc.fill = GridBagConstraints.BOTH;

        gc.insets = new Insets(2,2,2,2);
        add(new JScrollPane(newMessageArea), gc);

        ////////////////MESSAGE BUTTON/////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 2;

        gc.fill = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.LINE_START;
        add(sendMessageButton, gc);

        /////////////////FILE BUTTON//////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 2;

        gc.fill = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.LINE_END;
        add(sendFileButton, gc);
    }

}
