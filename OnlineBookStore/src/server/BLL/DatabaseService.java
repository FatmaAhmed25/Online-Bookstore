package server.BLL;
import server.model.Book;
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

    public static void createTables()
    {
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
                    "description VARCHAR(255)," +
                    "price DOUBLE," +
                    "quantity INT," +
                    "owner_id INT," +  // ID of the owner
                    "FOREIGN KEY (owner_id) REFERENCES Users(id))";  //Foreign key constraint for owner_id
            stmt.execute(createBooksTableSQL);

            // Create Requests table
            String createRequestsTableSQL = "CREATE TABLE IF NOT EXISTS Requests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "borrower_id INT," +  // ID of the borrower
                    "lender_id INT," +  // ID of the lender
                    "book_id INT," +     // ID of the book
                    "status VARCHAR(50)," +  // Status of the request
                    "FOREIGN KEY (borrower_id) REFERENCES Users(id)," +  // Foreign key constraint for borrower_id
                    "FOREIGN KEY (lender_id) REFERENCES Users(id)," +     // Foreign key constraint for lender_id
                    "FOREIGN KEY (book_id) REFERENCES Books(id))";        // Foreign key constraint for book_id
            stmt.execute(createRequestsTableSQL);

            // Create LentBooks table (many-to-many relationship between users and books)
            String createLentBooksTableSQL = "CREATE TABLE IF NOT EXISTS LentBooks (" +
                    "book_id INT," +
                    "user_id INT," +
                    "FOREIGN KEY (book_id) REFERENCES Books(id)," +
                    "FOREIGN KEY (user_id) REFERENCES Users(id)," +
                    "PRIMARY KEY (book_id, user_id))";  // Composite primary key to ensure uniqueness

            stmt.execute(createLentBooksTableSQL);
            String createChatRoomsTableSQL = "CREATE TABLE IF NOT EXISTS ChatRooms (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "requester_id INT," +  // ID of the requester
                    "lender_id INT," +      // ID of the lender
                    "senderUsername VARCHAR(255)," +
                    "recipientUsername VARCHAR(255)," +
                    "FOREIGN KEY (requester_id) REFERENCES Users(id)," +  // Foreign key constraint for requester_id
                    "FOREIGN KEY (lender_id) REFERENCES Users(id))";       // Foreign key constraint for lender_id
            stmt.execute(createChatRoomsTableSQL);
            String createMessagesTableSQL = "CREATE TABLE IF NOT EXISTS Messages (" +
                    "messageId INT AUTO_INCREMENT PRIMARY KEY," +
                    "chatRoomId INT," +
                    "senderUsername VARCHAR(255)," +
                    "recipientUsername VARCHAR(255)," +
                    "content VARCHAR(255)," +
                    "timestamp DATETIME," +
                    "FOREIGN KEY (chatRoomId) REFERENCES ChatRooms(id))";  // Foreign key constraint for chatRoomId
            stmt.execute(createMessagesTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
