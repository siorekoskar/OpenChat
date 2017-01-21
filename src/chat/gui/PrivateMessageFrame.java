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

        setTitle("Whisper");
        Dimension dim = new Dimension(350,300);
        setMinimumSize(dim);
        setPreferredSize(dim);

        messageArea = new JTextArea();
        messageArea.setMinimumSize(new Dimension(300,250));
        sendButton = new JButton("Send");
        cancelButton = new JButton("Cancel");
        messagePane = new JScrollPane(messageArea);
        sendButton.setPreferredSize(cancelButton.getPreferredSize());

        setLocationRelativeTo(null);

        layoutComponents();

        ////////////////////LISTENERS//////////////////
        sendButton.addActionListener(this);
        cancelButton.addActionListener(this);

    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 2;

        gc.fill = GridBagConstraints.BOTH;

        gc.insets = new Insets(2,2,2,2);
        add(messagePane, gc);

        /////////////////////////////////////

        gc.weightx =1 ;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 1;

        gc.fill = GridBagConstraints.CENTER;
        gc.gridwidth = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.LINE_START;
        add(sendButton, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        add(cancelButton, gc);
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
                listener.privateMessageOccured(message, messageTo);
                messageArea.setText("");

            } else if(clicked == cancelButton){
                setVisible(false);
                messageArea.setText("");
            }
        }
    }

    private static final String TEXT_SUBMIT = "text-submit";
    private static final String INSERT_BREAK = "insert-break";
    private void initialize() {
        InputMap input = messageArea.getInputMap();
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
        input.put(shiftEnter, INSERT_BREAK);  // input.get(enter)) = "insert-break"
        input.put(enter, TEXT_SUBMIT);

        ActionMap actions = messageArea.getActionMap();
        actions.put(TEXT_SUBMIT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
                setVisible(false);
            }
        });
    }

    public void setMessageArea(JTextArea messageArea) {
        this.messageArea = messageArea;
    }
}
