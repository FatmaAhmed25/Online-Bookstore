package server.DAL;

import server.BLL.DatabaseService;
import server.model.Book;
import server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public Book getBookByID(int bookId)
    {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE id = ?")) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getString("title"),rs.getString("author"),rs.getString("genre"),rs.getDouble("price"),rs.getString("description"),rs.getInt("owner_id"),rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public ArrayList<Book> getBooksByTitle(String title)
    {
        ArrayList<Book> books=new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE title = ?")) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next())
                {
                    books.add(new Book(rs.getString("title"),rs.getString("author"),rs.getString("genre"),rs.getDouble("price"),rs.getString("description"),rs.getInt("owner_id"),rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;

    }
    public  ArrayList<Book> getBooksByAuthor(String author)

    {
        ArrayList<Book> books=new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE author = ?")) {
            pstmt.setString(1, author);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next())
                {
                    books.add(new Book(rs.getString("title"),rs.getString("author"),rs.getString("genre"),rs.getDouble("price"),rs.getString("description"),rs.getInt("owner_id"),rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;

    }
    public ArrayList<Book> getBooksByGenre(String genre)
    {
        ArrayList<Book> books=new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE genre = ?")) {
            pstmt.setString(1, genre);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next())
                {
                    books.add(new Book(rs.getString("title"),rs.getString("author"),rs.getString("genre"),rs.getDouble("price"),rs.getString("description"),rs.getInt("owner_id"),rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;

    }


}
