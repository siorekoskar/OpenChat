package chat.controller;

import chat.gui.CreateChatEvent;
import chat.gui.MainFrame;
import chat.model.ChatRoom;
import chat.model.Client;
import chat.model.Message;

/**
 * Created by Oskar on 10/01/2017.
 */
public class ClientController{

    private Client chatClient;
    private MainFrame frame;



    public ClientController(String serverName, int serverPort, MainFrame frame, String username){
        chatClient = new Client(serverName, serverPort, this, username);
        this.frame = frame;
        chatClient.start();
    }

    public void sendMessage(Message msg){
        System.out.println(msg.getMessage() + "CONTROLER");
        chatClient.sendMessage(msg);
    }

    public void sendMsg(String msg){
        frame.sendMsg(msg);
    }
    public void disconnect(){
        chatClient.disconnect();
    }

    public void newChatCreated(CreateChatEvent ev){
        String chatName = ev.getChatName();
        String admin = ev.getAdmin();
        boolean isPrivate = ev.isPrivate();
        ChatRoom chatRoom = new ChatRoom(isPrivate, admin, chatName);
        System.out.println(chatRoom.toString());
    }

}