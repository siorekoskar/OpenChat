package main.java.pl.edu.agh.kis.openchat.gui.listenersinterfaces;

/**
 * Created by Oskar on 21/01/2017.
 */
public interface PrivateMessageListener {
    void privateMessageOccured(String msg, String messageTo);
}