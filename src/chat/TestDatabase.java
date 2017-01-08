package chat;

import chat.model.Database;
import chat.model.User;

import java.sql.SQLException;

/**
 * Created by Oskar on 08/01/2017.
 */
public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("Running Database test");

        Database db = new Database();

        try{
            db.connect();
        } catch (Exception e){
            e.printStackTrace();
        }

        db.addUser(new User("okularki111", "mama123"));
        db.addUser(new User("michael", "mama123"));

        try {
            db.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnect();

        System.out.println("Test end");
    }
}
