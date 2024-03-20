package server.service;

import java.sql.*;
import server.model.User;
public class UserService {
    private DatabaseService databaseService;
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    public  void registerUser(String name, String username, String password) throws Exception {
        databaseService.addUser(name, username, password);
    }

    public  User getUser(String username) throws SQLException {
        return databaseService.getUser(username);
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
}
