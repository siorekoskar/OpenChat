package main.java.pl.edu.agh.kis.openchat.controller;

import main.java.pl.edu.agh.kis.openchat.gui.CreateChatEvent;
import main.java.pl.edu.agh.kis.openchat.model.*;
import main.java.pl.edu.agh.kis.openchat.gui.GuiInterface;

import java.util.List;

/**
 * Created by Oskar on 10/01/2017.
 */
public class ClientController implements ClientControllerInterface{

    private ClientInterface chatClient;
    private GuiInterface frame;

    @Override
    public void setClient(ClientInterface client) {
        this.chatClient = client;
    }

    @Override
    public void setGui(GuiInterface gui) {
        this.frame = gui;
    }

    @Override
    public void sendYourChatRooms(List chatRooms){
        frame.setYourChatRooms(chatRooms);
    }

    @Override
    public void sendPrivateMsgToGui(String msg) {
        frame.sendPrivateMsgToGui(msg);
    }

    @Override
    public void notConnected(String msg) {
        frame.popNotConnectedDialog(msg);
    }

    @Override
    public void connected() {
        frame.popConnectedDialog();
    }

    @Override
    public void sendPrivateMessage(String msg, String messageTo, String messageFrom) {
        PrivateMessage privateMessage = new PrivateMessage(messageFrom, msg, messageTo);
        chatClient.sendPrivateMessage(privateMessage);
    }

    @Override
    public void sendUserLeft(List users) {
        frame.actualizeCauseUserLeftServer(users);
    }

    @Override
    public void sendDisallowed() {
        frame.popFail("Wrong username/password");
    }

    @Override
    public void sendNotAllowed() {
        frame.popFail("You are not allowed to join");
    }

    @Override
    public void sendAllowed(String user) {
        frame.popLoggedDialog(user);
    }

    @Override
    public void sendRegistered(String user) {
        frame.popSuccess("Registered as: " + user);
    }

    @Override
    public void sendExists() {
        frame.popSuccess("User already exists");
    }

    @Override
    public void sendInboxMessages(List messages) {
        frame.sendInboxMessages(messages);
    }

    @Override
    public void userInvited(String selected, String toChat, String username) {
        System.out.println("You invited " + selected);
        chatClient.userInvited(selected, toChat, username);
    }

    @Override
    public void sendMessage(Message msg) {
        chatClient.sendMessage(msg);
    }

    @Override
    public void sendMsg(Message msg) {
        frame.sendMsg(msg);
        if (msg.getType() == Message.CHATCONNECTION) {
            frame.setUsersInChat(msg.getChat().getUsersIn());
        }
    }

    @Override
    public void disconnect() {
        chatClient.disconnect();
    }

    @Override
    public void sendChatUsers(List users) {
        frame.setUsersInChat(users);
    }

    @Override
    public void sendLeft(List users) {
        frame.userLeftChat(users);
    }

    @Override
    public void newChatCreated(CreateChatEvent ev) {
        String chatName = ev.getChatName();
        String admin = ev.getAdmin();
        boolean isPrivate = ev.isPrivate();
        ChatRoom chatRoom = new ChatRoom(isPrivate, admin, chatName);
        Message msg = new Message(Message.CREATECHAT, admin, "", chatRoom);
        chatClient.sendMessage(msg);
    }

    @Override
    public void sendChat(ChatRoom chat) {
        frame.addChatToPanel(chat);
    }

    @Override
    public void sendUser(User user) {
        frame.addUserWhoJoinedServer(user);
    }

    @Override
    public void sendUsersRegisteredList(List list) {
        frame.sendUsersRegisteredList(list);
    }

    @Override
    public void sendAlreadyLogged(String username) {
        //frame.popAlreadyLoggedDialog(username);
        frame.popFail("User " + username + " already logged");
    }

    @Override
    public void sendChatExists(String chat){
        frame.popChatAlreadyExistsDialog(chat);
    }

    @Override
    public void sendChats(List chats, List privateList){
        frame.actualizeChats(chats, privateList);
    }

    @Override
    public void sendTooManyChats(){
        frame.popFail("Too many chats. Max is 2");
    }
}