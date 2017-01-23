package main.java.pl.edu.agh.kis.openchat.gui.listenersinterfaces;

import main.java.pl.edu.agh.kis.openchat.gui.FormEvent;

import java.util.EventListener;

/**
 * Created by Oskar on 08/01/2017.
 */
public interface FormListener extends EventListener {
    public void loginEventOccured(FormEvent e);
    public void registeredEventOccured(FormEvent e);

}

