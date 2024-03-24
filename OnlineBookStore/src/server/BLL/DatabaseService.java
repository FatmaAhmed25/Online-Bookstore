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
                    "description VARCHAR(255)," +
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
}
