package server.DAL;

import server.BLL.DatabaseService;
import server.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDAL {
    public void addBook(Book book) throws SQLException {
        String addBookSQL = "INSERT INTO Books (title, author, genre, quantity, price, owner_id,description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(addBookSQL)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setInt(6, book.getOwnerID());
            pstmt.setString(7, book.getDescription());

            pstmt.executeUpdate();
            System.out.println("Book added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding book to the database: " + e.getMessage());
            throw e;
        }
    }
    public void removeBook(int bookId) {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Books WHERE id = ?")) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
