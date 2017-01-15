package chat.model;

import chat.controller.ClientController;
import chat.gui.MainFrame;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Client  {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;

    private ClientController cg;

    private String server, username;
    private int port;

    public Client(String server, int port, ClientController frame, String username){
        this.server = server;
        this.port = port;
        this.cg = frame;
        this.username = username;
    }

    public boolean start(){
        try{
            socket = new Socket(server, port);
        } catch (Exception ec){
            System.out.println("error connecting");
            return false;
        }

        String msg = "Con accepted" + socket.getInetAddress() + ":" + socket.getPort();
        System.out.println(msg);

        try{
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO){
            System.out.println("Fail");
        }

        new ListenFromServer().start();

        try{
            sOutput.writeObject(username);
        } catch(IOException eIO){
            System.out.println("Exception loigin: "+ eIO);
            disconnect();
            return false;
        }

        return true;
    }

    public void display(String msg){
        cg.sendMsg(msg+"\n");
    }

    public void sendMessage(Message msg){
        try{
            System.out.println(msg.getMessage() + "SENDMESSAGECLIENT");
            sOutput.writeObject(msg);
        } catch (Exception e){}

    }

    public void disconnect() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {} // not much else I can do
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {} // not much else I can do
        try{
            if(socket != null) socket.close();
        }
        catch(Exception e) {} // not much else I can do


    }

    class ListenFromServer extends Thread{
        public void run(){
            while(true){
                try{
                    Object obj = sInput.readObject();

                    if(obj instanceof User){
                        cg.sendUser((User)obj);
                    }
                     else if (obj instanceof ChatRoom) {
                        System.out.println("lkoho");
                        cg.sendChat((ChatRoom)obj);
                    } else{
                        //String msg = (String) sInput.readObject();
                        String msg = (String) obj;
                        cg.sendMsg(msg);
                    }
                } catch(IOException e){
                    System.out.println("przypal");
                    break;
                } catch(ClassNotFoundException e){
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}