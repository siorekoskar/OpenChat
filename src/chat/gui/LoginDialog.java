package chat.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oskar on 08/01/2017.
 */
public class LoginDialog extends JDialog {
    private JButton loginButton;
    private JButton quitButton;
    private JTextField userField;
    private JPasswordField passField;
    private JButton registerButton;
    private RegisterDialog registerDialog;

    public LoginDialog(JFrame parent){
        super(parent, "Login", false);

        loginButton = new JButton("Login");
        quitButton = new JButton("Quit");
        registerButton = new JButton("Register");

        registerDialog = new RegisterDialog(parent);

        userField = new JTextField(10);
        passField = new JPasswordField(10);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerDialog.setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setEnabled(true);
            }
        });

        layoutControls();
        setSize(340, 250);
        setLocationRelativeTo(parent);

    }

    private void layoutControls(){
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        int space = 15;
        Border titledBorder = BorderFactory.createTitledBorder("Login details");
        Border spaceBorder = BorderFactory.createEmptyBorder(space,space,space,space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titledBorder));

        controlsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets leftPadding = new Insets(0, 15, 0, 0);
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

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        subPanel.add(loginButton);
        subPanel.add(quitButton);

        buttonsPanel.setLayout(new BorderLayout());

        buttonsPanel.add(subPanel, BorderLayout.EAST);
        buttonsPanel.add(registerButton, BorderLayout.WEST);

        Dimension btnSize = registerButton.getPreferredSize();
        quitButton.setPreferredSize(btnSize);
        loginButton.setPreferredSize(btnSize);

        //add sub panels to dialog
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

    }
}
