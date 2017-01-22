package chat.gui;

/**
 * Created by Oskar on 22/01/2017.
 */
public class GuiFactory {
    public static final int MAINFRAME = 1;
    public static GuiInterface returnGui(String host, int port, int type){
        if(type == MAINFRAME){
            return new MainFrame(host, port);
        }
        return null;
    }
}
