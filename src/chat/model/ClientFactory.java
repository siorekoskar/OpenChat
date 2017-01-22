package chat.model;

import chat.controller.ClientControllerInterface;

/**
 * Created by Oskar on 22/01/2017.
 */
public class ClientFactory {
    public static final int CLIENT = 1;

    public static ClientInterface returnClient(String serverName, int port, int type){
        if(type == CLIENT){
            return new Client(serverName, port);
        }
        return null;
    }
}
