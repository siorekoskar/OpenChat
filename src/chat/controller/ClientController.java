package chat.controller;

import chat.gui.CreateChatEvent;
import chat.gui.MainFrame;
import chat.model.ChatRoom;
import chat.model.Client;
import chat.model.Message;
import chat.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Oskar on 10/01/2017.
 */
public class ClientController{

    private Client chatClient;
    private MainFrame frame;

    public void notConnected(String msg){
        frame.notConnectedDialog(msg);
    }

    public void connected(){
        frame.connectedDialog();
    }

    public void sendPrivateMessage(String msg, String messageTo, String messageFrom){
        System.out.println("Message from: " + messageFrom + " to: " + messageTo + ": " + msg);
    }

    public void sendUserLeft(Message msg){
        frame.sendUserLeft(msg);
    }

    public void sendDisallowed(Message msg){
        frame.sendDissallowed(msg);
    }

    public void sendNotAllowed(Message msg){
        frame.sendNotAllowed();
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

    public void sendInboxMessages(List messages){
        frame.sendInboxMessages(messages);
    }

    public void userInvited(String selected, String username){
        System.out.println("You invited " + selected);
        chatClient.userInvited(selected, username);
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
        if(msg.getType() == Message.CHATCONNECTION){
            System.out.println("CZATCONE");

            frame.sendUsersOfChat(msg.getChat().getUsersIn());
        }
    }

    public void disconnect(){
        chatClient.disconnect();
    }

    public void sendChatUsers(Message msg){

        //List<String> items = Arrays.asList(msg.getUsersInChat().split("\\s*,\\s*"));

        System.out.println(msg.getUsersIn());
        String users = msg.getChat().getUsersAsString();
        System.out.println(users);
        //frame.sendChatUsers(users);
        frame.sendUsersOfChat(msg.users);
    }

    public void sendLeft(Message msg){
        frame.sendLeft( msg);
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