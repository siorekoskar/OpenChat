package main.java.pl.edu.agh.kis.openchat.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oskar on 23/01/2017.
 */
public class DatabaseTest {

    private Database database;

    @Before
    public void initialiseDb() throws Exception{
        database = new Database();
        database.connect("localhost:3306/user", "root", "shihtzu1");
        database.load();
    }

    @After
    public void disconnectDb() throws Exception{
        database.disconnect();
    }

    @Test
    public void checkIfUserExists() throws Exception{
        User a = new User("a", "mama123");
        assertEquals(true, database.checkIfUserExists(a));
    }

    @Test
    public void removeUser() throws Exception{
        User a = new User("a", "mama123");
        database.addUser(a);
        assertSame(a, database.removeUser(database.getUsers().size()-1));

    }

}