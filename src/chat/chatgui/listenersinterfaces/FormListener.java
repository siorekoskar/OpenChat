package chat.chatgui.listenersinterfaces;

import chat.chatgui.FormEvent;

import java.util.EventListener;

/**
 * Created by Oskar on 08/01/2017.
 */
public interface FormListener extends EventListener {
    public void formEventOccured(FormEvent e);
}
