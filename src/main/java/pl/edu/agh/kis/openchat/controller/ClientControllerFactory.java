package main.java.pl.edu.agh.kis.openchat.controller;

/**
 * Created by Oskar on 22/01/2017.
 */
public class ClientControllerFactory {
    public static final int CLIENTCONTROLLER = 1;

    public static ClientControllerInterface returnController(int type){
        if(type == CLIENTCONTROLLER){
            return new ClientController();
        }
        return null;
    }
}
