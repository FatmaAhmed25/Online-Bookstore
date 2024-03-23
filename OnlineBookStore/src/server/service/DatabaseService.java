package server.service;
import server.model.Book;
import server.model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            // SQL query to create the Book table
            String createBookTableSQL = "CREATE TABLE IF NOT EXISTS Book (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255)," +
                    "author VARCHAR(255)," +
                    "genre VARCHAR(255)," +
                    "price DECIMAL(10, 2)," +
                    "description VARCHAR(255))";
            stmt.execute(createBookTableSQL);
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
    public  void addBook(Book book) {

        System.out.println("hiii"+book);
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Book (title, author, genre, price, description) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setString(5, book.getDescription());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBook(int bookId) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Book WHERE id = ?")) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
