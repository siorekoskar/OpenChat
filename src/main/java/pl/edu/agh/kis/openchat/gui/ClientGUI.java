package main.java.pl.edu.agh.kis.openchat.gui;

import main.java.pl.edu.agh.kis.openchat.controller.ClientControllerFactory;
import main.java.pl.edu.agh.kis.openchat.controller.ClientControllerInterface;
import main.java.pl.edu.agh.kis.openchat.gui.listenersinterfaces.PrefsListener;
import main.java.pl.edu.agh.kis.openchat.model.ClientFactory;
import main.java.pl.edu.agh.kis.openchat.model.ClientInterface;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ClientGUI {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new MainFrame();
                ClientControllerInterface controller =
                        ClientControllerFactory.returnController(ClientControllerFactory.CLIENTCONTROLLER);

                GuiInterface gui = GuiFactory.returnGui(GuiFactory.MAINFRAME);

                new Launcher(controller, gui);
            }
        });
    }

}

class Launcher extends JFrame implements ActionListener{
    private JTextField hostField;
    private JTextField portField;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JButton enterButton;
    private JButton quitButton;

    private ClientControllerInterface controller;
    private GuiInterface gui;

    private PrefsListener prefsListener;

    Launcher(ClientControllerInterface controller, GuiInterface gui) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.controller = controller;
        this.gui = gui;

        hostField = new JTextField(10);
        portField = new JTextField(5);
        hostLabel = new JLabel("HOST: ");
        portLabel = new JLabel("PORT: ");
        enterButton = new JButton("Enter");
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(enterButton.getPreferredSize());


        WindowCloser windowCloser = new WindowCloser();

        setMinimumSize(new Dimension(250, 200));
        layoutComponents();
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(windowCloser);

        setDefaults("192.168.0.115", 3308);
        setVisible(true);

        enterButton.addActionListener(this);
        quitButton.addActionListener(this);
    }

    private void setDefaults(String host, Integer port) {
        hostField.setText(host);
        portField.setText(port.toString());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if(clicked == enterButton){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    ClientInterface client = ClientFactory.returnClient(hostField.getText(),
                            Integer.parseInt(portField.getText()), ClientFactory.CLIENT);

                    client.setController(controller);
                    gui.setClientController(controller);
                    gui.setHostAndPort(hostField.getText(), Integer.parseInt(portField.getText()));
                    controller.setClient(client);
                    controller.setGui(gui);
                    client.start();

                }
            });
            dispose();
        } else if (clicked == quitButton){
            dispose();
            System.exit(0);
        }
    }

    private class WindowCloser extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
        }
    }

    private void layoutComponents() {
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        int space = 8;
        Border titledBorder = BorderFactory.createTitledBorder("Connection details:");
        Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titledBorder));

        controlsPanel.setLayout(new GridBagLayout());

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        Insets rightPadding = new Insets(0, 0, 0, 15);
        Insets noPadding = new Insets(0, 0, 0, 0);


        /////////////////////////////////
        gc.gridy = 0;

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;

        /////////////////////
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(hostLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(hostField, gc);

        /////////////////////
        gc.gridy++;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlsPanel.add(portLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlsPanel.add(portField, gc);

        /////////BUTTONS PANEL//////////////

        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(enterButton, BorderLayout.WEST);
        buttonsPanel.add(quitButton, BorderLayout.EAST);

        //ADD SUB PANELS TO FRAME////
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }
}
