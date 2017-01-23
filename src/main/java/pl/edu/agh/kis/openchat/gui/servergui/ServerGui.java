package main.java.pl.edu.agh.kis.openchat.gui.servergui;


import main.java.pl.edu.agh.kis.openchat.controller.DBControllerInterface;
import main.java.pl.edu.agh.kis.openchat.controller.DataBaseControllerFactory;
import main.java.pl.edu.agh.kis.openchat.model.Server;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Oskar on 22/01/2017.
 */
public class ServerGui {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DBControllerInterface dbController =
                        DataBaseControllerFactory.returnDBController(DataBaseControllerFactory.DBCONTROLLER);

                new Launcher(dbController);
            }
        });
    }

}

class Launcher extends JFrame implements ActionListener{
    private JTextField hostField;
    private JTextField portField;
    private JTextField adminField;
    private JTextField passwordField;

    private JLabel hostLabel;
    private JLabel portLabel;
    private JLabel adminLabel;
    private JLabel passwordLabel;

    private JButton startButton;
    private JButton quitButton;

    private DBControllerInterface dbController;

    Launcher(DBControllerInterface dbController){
        super("Start server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dbController = dbController;

        hostField = new JTextField(12);
        portField = new JTextField(5);
        adminField = new JTextField(10);
        passwordField = new JPasswordField(10);

        hostLabel = new JLabel("HOST: ");
        portLabel = new JLabel("PORT: ");
        adminLabel = new JLabel("Login: ");
        passwordLabel = new JLabel("Password: ");

        startButton = new JButton("Start");
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(startButton.getPreferredSize());

        WindowCloserS windowCloser = new WindowCloserS();

        setMinimumSize(new Dimension(270,200));

        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(windowCloser);

        startButton.addActionListener(this);
        quitButton.addActionListener(this);

        setDefaults("localhost:3306/user", 3308, "root", "shihtzu1");

        layoutComponents();
    }

    private void setDefaults(String host, Integer port, String adm, String pass){
        hostField.setText(host);
        portField.setText(port.toString());
        adminField.setText(adm);
        passwordField.setText(pass);
    }

    private void layoutComponents(){
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        int space = 8;
        Border titledBorder = BorderFactory.createTitledBorder("Connection details:");
        Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titledBorder));
        controlsPanel.setLayout(new GridBagLayout());

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();


        ////////////////////////
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill= GridBagConstraints.NONE;

        /////////////////////
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        controlsPanel.add(hostLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        controlsPanel.add(hostField, gc);

        /////////////////////
        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        controlsPanel.add(portLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        controlsPanel.add(portField, gc);

        /////////////////////
        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        controlsPanel.add(adminLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        controlsPanel.add(adminField, gc);

        /////////////////////
        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        controlsPanel.add(passwordLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        controlsPanel.add(passwordField, gc);

        /////////BUTTONS PANEL//////////////

        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(startButton, BorderLayout.WEST);
        buttonsPanel.add(quitButton, BorderLayout.EAST);

        //ADD SUB PANELS TO FRAME////
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if(clicked == startButton){
            setVisible(false);
                    String password = passwordField.getText();//pass.toString();
                    String host = hostField.getText();
                    Integer port = Integer.parseInt(portField.getText());
                    String admin = adminField.getText();

                    Server server = new Server(port,
                            host, admin ,password, dbController);

            RunServer rs = new RunServer(server);
            Thread rsT = new Thread(rs);
            rsT.start();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ServerStop();
                }
            });

            server.start();

        } else if( clicked == quitButton){
            dispose();
            System.exit(0);
        }
    }



    private class WindowCloserS extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
        }
    }

}

class ServerStop extends JFrame implements ActionListener{
    private JLabel label;
    private JButton button;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked =(JButton) e.getSource();

        if(clicked == button){
            dispose();
            System.exit(0);
        }
    }

    ServerStop(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel("Server running");
        button  = new JButton("STOP SERVER");

        setMinimumSize(new Dimension(100,100));
        setLocationRelativeTo(null);

        add(label, BorderLayout.NORTH);
        add(button, BorderLayout.SOUTH);

        button.addActionListener(this);
        setVisible(true);
    }
}

class RunServer implements Runnable{
    Server server;
    RunServer(Server server){
        this.server = server;
    }

    @Override
    public void run() {
        server.start();
    }
}
