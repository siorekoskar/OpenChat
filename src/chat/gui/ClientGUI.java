package chat.gui;

import chat.gui.listenersinterfaces.PrefsListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

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
                new Launcher();
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
    //private Preferences prefs;

    private PrefsListener prefsListener;

    public Launcher() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hostField = new JTextField(10);
        portField = new JTextField(5);
        hostLabel = new JLabel("HOST: ");
        portLabel = new JLabel("PORT: ");
        enterButton = new JButton("Enter");
        quitButton = new JButton("Quit");
        quitButton.setPreferredSize(enterButton.getPreferredSize());

        //prefs = Preferences.userRoot().node("connectionDetails");

        WindowCloser windowCloser = new WindowCloser();

        setMinimumSize(new Dimension(250, 200));
        layoutComponents();
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(windowCloser);

        /*setPrefsListener(new PrefsListener() {
            @Override
            public void preferencesSet(String host, int port) {
                prefs.put("host", host);
                prefs.putInt("port", port);
            }
        });*/

        //String host = prefs.get("host", "192.168.0.115");
        //Integer port = prefs.getInt("port", 3308);
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
                    new MainFrame(hostField.getText(), Integer.parseInt(portField.getText()));
                }
            });
            dispose();
        } else if (clicked == quitButton){
            dispose();
            System.gc();
            System.exit(0);
        }
    }

    private class WindowCloser extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
            System.gc();
        }
    }

    private void setPrefsListener(PrefsListener listener){
        this.prefsListener = listener;
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
        Insets leftPadding = new Insets(0, 15, 0, 0);
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
