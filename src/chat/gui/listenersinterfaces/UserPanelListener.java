package chat.gui.listenersinterfaces;

import chat.gui.CreateChatEvent;

/**
 * Created by Oskar on 08/01/2017.
 */
public interface UserPanelListener {
    void logoutEventOccured();
    void chatboxEventOccured();
    void inboxEventOccured();
    void usersEventOccured();
}
