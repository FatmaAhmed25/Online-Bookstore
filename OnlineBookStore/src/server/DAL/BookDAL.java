package server.DAL;

import server.BLL.DatabaseService;
import server.model.Book;
import server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookDAL
{
    public ArrayList<Book> getCurrentBorrowedBooks() {
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE id IN (SELECT DISTINCT book_id FROM Requests WHERE status = 'Accepted')";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                borrowedBooks.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("owner_id"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
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

    public Book getBookByID(int bookId) {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE id = ?")) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getDouble("price"), rs.getString("description"), rs.getInt("owner_id"), rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Book> getBooksByTitle(String title) {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE title = ? AND quantity > 0")) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getDouble("price"), rs.getString("description"), rs.getInt("owner_id"), rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    public ArrayList<Book> getBooksByAuthor(String author) {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE author = ? AND quantity > 0")) {
            pstmt.setString(1, author);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getDouble("price"), rs.getString("description"), rs.getInt("owner_id"), rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;

    }

    public ArrayList<Book> getBooksByGenre(String genre) {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE genre = ? AND quantity > 0")) {
            pstmt.setString(1, genre);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getDouble("price"), rs.getString("description"), rs.getInt("owner_id"), rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;

    }


    public boolean bookBelongsToUser(int bookId, int lenderId) {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Books WHERE owner_id = ? AND id= ?")) {
            pstmt.setInt(1, lenderId);
            pstmt.setInt(2, bookId);


            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void decreaseBookQuantity(int id) {
        // Define the SQL update statement to decrement the quantity
        String sql = "UPDATE Books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the title parameter in the SQL statement
            pstmt.setInt(1, id);

            // Execute the update statement
            int rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE quantity > 0";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                borrowedBooks.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("owner_id"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

}