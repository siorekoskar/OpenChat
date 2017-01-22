package chat.model;

import chat.controller.ClientController;
import chat.controller.ClientControllerInterface;
import chat.gui.MainFrame;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Client implements ClientInterface {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;

    private ClientControllerInterface cg;

    private String server;
    private int port;

    public void setController(ClientControllerInterface cg){
        this.cg = cg;
    }


    public Client(String server, int port) {
        this.server = server;
        this.port = port;

    }

    public boolean start() {
        if(cg!= null) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(server, port), 3000);
                cg.connected();

            } catch (UnknownHostException e) {
                cg.notConnected("Wrong format of ip");
            } catch (Exception ec) {
                cg.notConnected("Server unreachable");
                return false;
            }
            String msg = "Con accepted" + socket.getInetAddress() + ":" + socket.getPort();
            System.out.println(msg);

            try {
                sInput = new ObjectInputStream(socket.getInputStream());
                sOutput = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException eIO) {
                System.out.println("Fail");
            }

            new ListenFromServer().start();

            return true;
        } else{
            return false;
        }
    }


    public void sendMessage(Message msg) {
        try {
            if(sOutput!=null)
                sOutput.writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPrivateMessage(PrivateMessage msg){
        try{
           if(sOutput!=null) sOutput.writeObject(msg);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (sInput != null) sInput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) sOutput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        } // not much else I can do
        //sendMessage(new Message(Message.DISCONNECT));


    }

    public void userInvited(String userInvited,String toChat, String admin){
        Message msg = new Message(Message.USERINVITED, admin, userInvited, toChat);
        sendMessage(msg);
    }

    class ListenFromServer extends Thread {
        public void run() {
            while (true) {
                try {
                    Object obj = sInput.readObject();


                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        int type = msg.getType();

                        switch (type) {
                            case Message.ALLOWED:
                                cg.sendAllowed(msg.getUser());
                                break;
                            case Message.DISALLOWED:
                                cg.sendDisallowed();
                                break;
                            case Message.REGISTER:
                                cg.sendRegistered(msg.getUser());
                                break;
                            case Message.EXISTS:
                                cg.sendExists();
                                break;
                            case Message.CHATCONNECTION:
                                cg.sendMsg(msg);
                                cg.sendChatUsers(msg.users);
                                break;
                            case Message.CHATLEFT:
                                cg.sendLeft(msg.getUsersIn());
                                break;
                            case Message.NOTALLOWED:
                                cg.sendNotAllowed();
                                break;
                            case Message.DISCONNECT:
                                cg.sendUserLeft(msg.getUsersIn());
                                break;
                            case Message.PRIVATEMESSAGE:
                                cg.sendInboxMessages(msg.getListOfUndeclared());
                                break;
                            case Message.USERSREGISTEREDLIST:
                                cg.sendUsersRegisteredList(msg.getUsersIn());
                                break;
                            case Message.ALREADYLOGGED:
                                cg.sendAlreadyLogged(msg.getUser());
                                break;
                            case Message.CHATROOMEXISTS:
                                cg.sendChatExists(msg.getMessage());
                                break;
                            case Message.GETCHATROOMS:
                                cg.sendYourChatRooms(msg.getListOfUndeclared());
                                break;
                            case Message.CHATACTUALISE:
                                cg.sendChats(msg.getListOfUndeclared(), msg.getPrivateList());
                                break;
                            case Message.TOOMANYCHATS:
                                cg.sendTooManyChats();
                                break;
                            default:
                                cg.sendMsg(msg);
                                break;
                        }

                    } else if (obj instanceof User) {
                        cg.sendUser((User) obj);
                    } else if (obj instanceof ChatRoom) {
                        cg.sendChat((ChatRoom) obj);
                    } else if (obj instanceof PrivateMessage){
                        PrivateMessage pm = (PrivateMessage)obj;
                        String messageFrom = pm.getMessageFrom();
                        String message = pm.getMessage();
                        String messageConstructed = messageFrom + ": " + message;
                        cg.sendPrivateMsgToGui(messageConstructed);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    cg.notConnected("Connection lost");
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    cg.notConnected("Unknown file received");
                    break;
                }
            }
        }
    }
}