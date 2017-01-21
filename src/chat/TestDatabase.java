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
            db.connect("localhost:3306/user","root", "shihtzu1");
        } catch (Exception e){
            e.printStackTrace();
        }

        User okularki = new User("okularki111", "mama123");
        User test = new User("test", "test");

        try {
            if (!db.checkIfUserExists(okularki)){
                db.addUser(okularki);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } {
        }

        try {
            db.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            System.out.println(db.checkIfUserExists(okularki));
            System.out.println(db.checkIfUserExists(test));
        } catch(SQLException e){
            e.printStackTrace();
        }

        try {
            db.load();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnect();

        System.out.println("Test end");
    }
}
