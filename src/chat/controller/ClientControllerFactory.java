package chat.controller;

import chat.gui.MainFrame;

/**
 * Created by Oskar on 22/01/2017.
 */
public class ClientControllerFactory {
    public static final int CLIENTCONTROLLER = 1;

    public static ClientControllerInterface returnController(String serverName, int serverPort,
                                                      MainFrame frame, int type){
        if(type == CLIENTCONTROLLER){
            return new ClientController(serverName, serverPort, frame);
        }
        return null;
    }
}
