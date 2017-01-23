package main.java.pl.edu.agh.kis.openchat.gui;

/**
 * Created by Oskar on 22/01/2017.
 */
public class GuiFactory {
    public static final int MAINFRAME = 1;
    public static GuiInterface returnGui(int type){
        if(type == MAINFRAME){
            return new MainFrame();
        }
        return null;
    }
}