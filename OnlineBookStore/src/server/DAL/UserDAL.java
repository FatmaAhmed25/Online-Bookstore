package server.DAL;

import server.BLL.DatabaseService;
import server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAL {
    public  synchronized void addUser(String name, String username, String password) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users(name, username, password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        }
    }
    public synchronized User getUser(String username) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?")) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"),rs.getString("name"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }
    public synchronized User getUserById(int id) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"),rs.getString("name"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}
