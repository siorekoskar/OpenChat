package main.java.pl.edu.agh.kis.openchat.controller;

/**
 * Created by Oskar on 22/01/2017.
 */

/**
 * Factory of database controllers
 */
public class DataBaseControllerFactory {
    public static final int DBCONTROLLER = 1;
    public static DBControllerInterface returnDBController(int type){
        if(type == DBCONTROLLER){
            return new DbController();
        }
        return null;
    }
}
