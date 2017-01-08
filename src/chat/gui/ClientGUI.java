package chat.gui;

import javax.swing.*;

/**
 * Created by Oskar on 07/01/2017.
 */
public class ClientGUI {
    public static void main(String[] args) {

        try
        {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        }
        catch(Exception e){
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
