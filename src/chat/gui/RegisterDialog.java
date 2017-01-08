package chat.gui;

import chat.gui.listenersinterfaces.FormListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 08/01/2017.
 */
public class RegisterDialog extends JDialog {

    private JButton quitButton;
    private JTextField userField;
    private JPasswordField passField;
    private JButton registerButton;

    private FormListener formListener;

    public RegisterDialog(JFrame parent){
        super(parent, "Register", false);

        quitButton = new JButton("Quit");
        registerButton = new JButton("Register");

        userField = new JTextField(10);
        passField = new JPasswordField(10);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userField.getText();
                char[] pass = passField.getPassword();
                String password = new String(pass);

                FormEvent ev = new FormEvent(this, login, password);

                if(formListener!= null){
                    formListener.registeredEventOccured(ev);
                }
                setVisible(false);
            }
        });

        layoutControls();
        setSize(340, 250);
        setLocationRelativeTo(parent);
    }

    public void setFormListener(FormListener formListener){
        this.formListener = formListener;
    }


    //////////////////////LAYOUT//////////////////////////
    private void layoutControls(){
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        int space = 15;
        Border titledBorder = BorderFactory.createTitledBorder("Register");
        Border spaceBorder = BorderFactory.createEmptyBorder(space,space,space,space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titledBorder));

        controlsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets noPadding = new Insets(0, 0, 0, 0);

        gc.gridy = 0;

        ////////////first row//////////

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(new JLabel("User: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(userField,gc);

        ///////////next row/////////
        gc.gridy++;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;

        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(new JLabel("Password: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(passField,gc);

        ///////////////////////buttons panel///////////

        buttonsPanel.setLayout(new BorderLayout());

        buttonsPanel.add(quitButton, BorderLayout.EAST);
        buttonsPanel.add(registerButton, BorderLayout.WEST);

        Dimension btnSize = registerButton.getPreferredSize();
        quitButton.setPreferredSize(btnSize);
        registerButton.setPreferredSize(btnSize);

        //add sub panels to dialog
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

    }

}
