package server.BLL;

import java.sql.*;

import server.DAL.UserDAL;
import server.model.Book;
import server.model.User;

public class UserService {
    UserDAL userDB = new UserDAL();

    public  User getUser(String username) throws SQLException {
        return userDB.getUser(username);
    }

    public  void registerUser(String name, String username, String password) throws Exception {
        userDB.addUser(name, username, password);
    }

    public  boolean authenticateUser(String username, String password) throws SQLException {
        User user = getUser(username);
        return user != null && user.getPassword().equals(password);
    }

    public  boolean userExists(String username) throws SQLException {
        // Check if the user with the given username exists in the database
        User user = getUser(username);
        return user != null;
    }



    public User getUserById(int id) throws SQLException {
        User user = userDB.getUserById(id);
        if(user==null)
        {
            throw new NullPointerException("User not found");
        }
        return user;
    }
}


