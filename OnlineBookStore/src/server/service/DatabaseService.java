package server.service;
import server.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseService {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("OnlineBookStore/src/server/config");
            properties.load(input);
            URL = properties.getProperty("db.URL");
            USERNAME = properties.getProperty("db.USERNAME");
            PASSWORD = properties.getProperty("db.PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void createTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "username VARCHAR(255) UNIQUE," +
                    "password VARCHAR(255))";
            stmt.execute(createUserTableSQL);
            String createBooksTableSQL = "CREATE TABLE IF NOT EXISTS Books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255)," +
                    "author VARCHAR(255)," +
                    "genre VARCHAR(255)," +
                    "price DOUBLE," +
                    "quantity INT," +
                    "owner_id INT," +  // ID of the owner
                    "FOREIGN KEY (owner_id) REFERENCES Users(id))";  //Foreign key constraint for owner_id
            stmt.execute(createBooksTableSQL);

            //many-to-many relationship between users and books (lentBy relationship)
            String createLentBooksTableSQL = "CREATE TABLE IF NOT EXISTS LentBooks (" +
                    "book_id INT," +
                    "user_id INT," +
                    "FOREIGN KEY (book_id) REFERENCES Books(id)," +
                    "FOREIGN KEY (user_id) REFERENCES Users(id)," +
                    "PRIMARY KEY (book_id, user_id))";  // Composite primary key to ensure uniqueness
            stmt.execute(createLentBooksTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String name, String username, String password) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users(name, username, password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        }
    }
    public User getUser(String username) throws SQLException {
        try (Connection conn = getConnection();
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
    public void addBookToDatabase(Connection conn, String title, String author, String genre, int quantity, double price, int ownerId) throws SQLException {
        String addBookSQL = "INSERT INTO Books (title, author, genre, quantity, price, owner_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(addBookSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setInt(4, quantity);
            pstmt.setDouble(5, price);
            pstmt.setInt(6, ownerId);

            pstmt.executeUpdate();
            System.out.println("Book added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding book to the database: " + e.getMessage());
            throw e;
        }
    }
}
