package chat.gui;

import chat.gui.listenersinterfaces.PrivateMessageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 20/01/2017.
 */
public class PrivateMessageFrame extends JFrame implements ActionListener{
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton cancelButton;
    private JScrollPane messagePane;
    private String messageTo;



    private PrivateMessageListener listener;

    PrivateMessageFrame(){
        super();

        Dimension dim = new Dimension(350,300);
        setMinimumSize(dim);
        setPreferredSize(dim);

        messageArea = new JTextArea();// + userTitle);
        messageArea.setMinimumSize(new Dimension(300,250));
        sendButton = new JButton("Send");
        cancelButton = new JButton("Cancel");
        messagePane = new JScrollPane(messageArea);
        //messagePane.setMinimumSize(new Dimension(300, 250));
        sendButton.setPreferredSize(cancelButton.getPreferredSize());

        setLocationRelativeTo(null);

        JPanel jPanel = new JPanel();
        jPanel.setMinimumSize(new Dimension(300,0));
        jPanel.add(sendButton, BorderLayout.WEST);
        jPanel.add(cancelButton, BorderLayout.EAST);

        add(messagePane, BorderLayout.NORTH);
        add(jPanel, BorderLayout.SOUTH);

    }

    void setMessageTo(String user){
        messageTo = user;
        messagePane.setBorder(BorderFactory.createTitledBorder("Message to: " + user));
    }

    public void setListener(PrivateMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        System.out.println("CLICKED!");
        if(obj instanceof JButton){
            JButton clicked = (JButton) obj;
            if(clicked == sendButton){
                String message = messageArea.getText();

                listener.privateMessageOccured(message);

            } else if(clicked == cancelButton){
                setVisible(false);
            }
        }
    }
}
