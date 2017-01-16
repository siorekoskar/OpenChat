package chat.gui;

import chat.gui.listenersinterfaces.MessageListener;
import chat.model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 07/01/2017.
 */
public class MessagePanel extends JPanel implements ActionListener{

    private JButton sendMessageButton;
    private JButton sendFileButton;
    private JTextArea inputTextArea;
    private JTextArea messagesArea;


    private MessageListener messageListener;

    MessagePanel(){
        inputTextArea = new JTextArea();
        messagesArea = new JTextArea();
        sendFileButton = new JButton("Upload File");
        sendMessageButton = new JButton("Send");

        messagesArea.setEditable(false);
        messagesArea.setBorder(BorderFactory.createTitledBorder("CHATNAME"));
        inputTextArea.setBorder(BorderFactory.createTitledBorder("Message"));
        Dimension buttonDim = new Dimension(100, 40);
        sendFileButton.setPreferredSize(buttonDim);
        sendMessageButton.setPreferredSize(buttonDim);

        add(messagesArea);

        sendFileButton.addActionListener(this);
        sendMessageButton.addActionListener(this);
        initialize();

        layoutComponents();

    }

    //////////////////////LAYOUT//////////////////////////

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
        add(new JScrollPane(inputTextArea), gc);

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

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton clicked = (JButton)e.getSource();

        if(messageListener!=null) {
            if (clicked == sendMessageButton) {
                String msg = inputTextArea.getText();
                messageListener.messageSent(msg);
                inputTextArea.setText("");

            }
            else if(clicked == sendFileButton){
                messageListener.fileSent();
            }
        }
    }

    void append(Message str){
        if(str.getType() == Message.CHATCONNECTION){

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    clean();
                    messagesArea.append(str.getMessage());
                }
            });
            System.out.println("madasraka");
        } else{
            messagesArea.append(str.getMessage() +"\n");
        }


    }

    private static final String TEXT_SUBMIT = "text-submit";
    private static final String INSERT_BREAK = "insert-break";
    private void initialize() {
        InputMap input = inputTextArea.getInputMap();
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
        input.put(shiftEnter, INSERT_BREAK);  // input.get(enter)) = "insert-break"
        input.put(enter, TEXT_SUBMIT);

        ActionMap actions = inputTextArea.getActionMap();
        actions.put(TEXT_SUBMIT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageButton.doClick();
            }
        });
    }

    void clean(){
        messagesArea.setText("");
    }


}




