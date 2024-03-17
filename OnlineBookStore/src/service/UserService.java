package service;

import java.sql.*;
import model.User;
public class UserService {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public  void registerUser(String name, String username, String password) throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users(name, username, password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        }
    }

    public  User getUser(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?")) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("name"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
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
