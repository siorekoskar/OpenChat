package chat.controller;

import chat.gui.CreateChatEvent;
import chat.gui.MainFrame;
import chat.model.ChatRoom;
import chat.model.Client;
import chat.model.Message;
import chat.model.User;

import java.util.List;

/**
 * Created by Oskar on 10/01/2017.
 */
public class ClientController{

    private Client chatClient;
    private MainFrame frame;

    public void sendDisallowed(Message msg){
        frame.sendDissallowed(msg);
    }


    public void sendAllowed(Message msg){
        frame.sendAllowed( msg);
    }

    public void sendRegistered(Message msg){
        frame.sendRegistered(msg);
    }

    public void sendExists(Message msg){
        frame.sendExists(msg);
    }

    public ClientController(String serverName, int serverPort, MainFrame frame){
        chatClient = new Client(serverName, serverPort, this);
        this.frame = frame;
        chatClient.start();
    }

    public void sendMessage(Message msg){
        chatClient.sendMessage(msg);
    }

    public void sendMsg(Message msg){
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
        Message msg = new Message(Message.CREATECHAT, admin, "", chatRoom);
        chatClient.sendMessage(msg);
        //ystem.out.println(chatRoom.toString());
    }

    public void sendChat(ChatRoom chat){
        frame.sendChat(chat);
    }

    public void sendUser(User user){
        frame.sendUser(user);
    }

}