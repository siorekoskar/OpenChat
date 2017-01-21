package chat.model;

import chat.controller.ClientController;
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
public class Client {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;

    private ClientController cg;

    private String server, username;
    private int port;



    public Client(String server, int port, ClientController frame, String username) {
        this.server = server;
        this.port = port;
        this.cg = frame;
        this.username = username;
    }

    public Client(String server, int port, ClientController frame) {
        this.server = server;
        this.port = port;
        this.cg = frame;

    }

    public boolean start() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server,port),3000);
            cg.connected();

        }catch(UnknownHostException e){;
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
    }

    public void display(String msg) {
        System.out.println(msg);
    }

    public void sendMessage(Message msg) {
        try {
            System.out.println(msg.getMessage() + "SENDMESSAGECLIENT");
            sOutput.writeObject(msg);
        } catch (Exception e) {
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

    public void userInvited(String userInvited, String admin){
        Message msg = new Message(Message.USERINVITED, admin, userInvited);
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
                                cg.sendAllowed(msg);
                                break;
                            case Message.DISALLOWED:
                                cg.sendDisallowed(msg);
                                break;
                            case Message.REGISTER:
                                cg.sendRegistered(msg);
                                break;
                            case Message.EXISTS:
                                cg.sendExists(msg);
                                break;
                            case Message.CHATCONNECTION:
                                cg.sendMsg(msg);
                                cg.sendChatUsers(msg);
                                break;
                            case Message.CHATLEFT:
                                cg.sendLeft(msg);
                                break;
                            case Message.NOTALLOWED:
                                cg.sendNotAllowed(msg);
                                break;
                            case Message.DISCONNECT:
                                cg.sendUserLeft(msg);
                                break;
                            case Message.PRIVATEMESSAGE:
                                cg.sendInboxMessages(msg.getListOfUndeclared());
                            default:
                                cg.sendMsg(msg);
                                break;
                        }

                    } else if (obj instanceof User) {
                        cg.sendUser((User) obj);
                    } else if (obj instanceof ChatRoom) {
                        cg.sendChat((ChatRoom) obj);
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