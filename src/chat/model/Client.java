package chat.model;

import chat.controller.ClientController;
import chat.gui.MainFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Client  {
    private DataInputStream sInput;
    private DataOutputStream sOutput;
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
            sInput = new DataInputStream(socket.getInputStream());
            sOutput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException eIO){
            System.out.println("Fail");
        }

        new ListenFromServer().start();

        return true;
    }

    public void display(String msg){
        cg.sendMsg(msg+"\n");
    }

    public void sendMessage(String msg){
        try{
            sOutput.writeUTF(username + ": " +msg);
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
                    String msg = sInput.readUTF();
                    cg.sendMsg(msg);
                } catch(IOException e){
                    System.out.println("przypal");
                }
            }
        }
    }
}