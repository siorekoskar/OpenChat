package chat.controller;

import chat.gui.CreateChatEvent;
import chat.gui.MainFrame;
import chat.model.*;

import java.util.List;

/**
 * Created by Oskar on 10/01/2017.
 */
public class ClientController {

    private Client chatClient;
    private MainFrame frame;

    public void sendPrivateMsgToGui(String msg) {
        frame.sendPrivateMsgToGui(msg);
    }

    public void notConnected(String msg) {
        frame.notConnectedDialog(msg);
    }

    public void connected() {
        frame.connectedDialog();
    }

    public void sendPrivateMessage(String msg, String messageTo, String messageFrom) {
        PrivateMessage privateMessage = new PrivateMessage(messageFrom, msg, messageTo);
        chatClient.sendPrivateMessage(privateMessage);
    }

    public void sendUserLeft(List users) {
        frame.sendUserLeft(users);
    }

    public void sendDisallowed() {
        frame.popWrongDataDialog();
    }

    public void sendNotAllowed() {
        frame.sendNotAllowed();
    }

    public void sendAllowed(Message msg) {
        frame.popLoggedDialog(msg);
    }

    public void sendRegistered(Message msg) {
        frame.popRegisteredDialog(msg);
    }

    public void sendExists() {
        frame.popAlreadyExistsDialog();
    }

    public void sendInboxMessages(List messages) {
        frame.sendInboxMessages(messages);
    }

    public void userInvited(String selected, String username) {
        System.out.println("You invited " + selected);
        chatClient.userInvited(selected, username);
    }

    public ClientController(String serverName, int serverPort, MainFrame frame) {
        chatClient = new Client(serverName, serverPort, this);
        this.frame = frame;
        chatClient.start();
    }

    public void sendMessage(Message msg) {
        chatClient.sendMessage(msg);
    }

    public void sendMsg(Message msg) {
        frame.sendMsg(msg);
        if (msg.getType() == Message.CHATCONNECTION) {
            frame.sendUsersOfChat(msg.getChat().getUsersIn());
        }
    }

    public void disconnect() {
        chatClient.disconnect();
    }

    public void sendChatUsers(List users) {
        frame.sendUsersOfChat(users);
    }

    public void sendLeft(Message msg) {
        frame.sendLeft(msg);
    }

    public void newChatCreated(CreateChatEvent ev) {
        String chatName = ev.getChatName();
        String admin = ev.getAdmin();
        boolean isPrivate = ev.isPrivate();
        ChatRoom chatRoom = new ChatRoom(isPrivate, admin, chatName);
        Message msg = new Message(Message.CREATECHAT, admin, "", chatRoom);
        chatClient.sendMessage(msg);
        //ystem.out.println(chatRoom.toString());
    }

    public void sendChat(ChatRoom chat) {
        frame.sendChat(chat);
    }

    public void sendUser(User user) {
        frame.sendUser(user);
    }

    public void sendUsersRegisteredList(List list) {
        frame.sendUsersRegisteredList(list);
    }

    public void sendAlreadyLogged(String username) {
        frame.popAlreadyLoggedDialog(username);
    }

}