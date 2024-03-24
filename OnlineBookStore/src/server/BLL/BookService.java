package server.BLL;

import server.DAL.BookDAL;
import server.model.Book;

import java.sql.SQLException;

public class BookService {
    private BookDAL bookDB = new BookDAL();
    public void removeBook(int bookId) {
        bookDB.removeBook(bookId);
    }

    public void addBook(Book book) throws SQLException {
        bookDB.addBook(book);
    }
}
