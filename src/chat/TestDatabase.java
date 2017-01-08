package chat;

import chat.model.Database;
import chat.model.User;

/**
 * Created by Oskar on 08/01/2017.
 */
public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("Running Database test");

        Database db = new Database();

        db.addUser(new User("okularki111", "mama123"));
        db.addUser(new User("michael", "mama123"));

        System.out.println("Test end");
    }
}
