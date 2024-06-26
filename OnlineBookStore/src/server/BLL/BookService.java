package server.BLL;

import server.DAL.BookDAL;
import server.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookService {
    private BookDAL bookDB = new BookDAL();

    public ArrayList<Book> getAvailableBooks() {
        return bookDB.getAvailableBooks();
    }
    public ArrayList<Book> getCurrentBorrowedBooks() {
        return bookDB.getCurrentBorrowedBooks();
    }
    public boolean removeBook(int bookId,int userId) {
        Book book=bookDB.getBookByID(bookId);
        if(book == null)
        {
            throw new NullPointerException("Book not found");
        }
        if(book.getOwnerID()==userId)
        {
            bookDB.removeBook(bookId);
            return true;
        }
        else return false;


    }
    public Book getBookByID(int bookID)
    {
        return bookDB.getBookByID(bookID);
    }

    public void addBook(Book book) throws SQLException {
        bookDB.addBook(book);
    }
    public ArrayList<Book> getBooksByTitle(String title)
    {
        ArrayList<Book> books=new ArrayList<>();
        books=bookDB.getBooksByTitle(title);
        for(int i=0;i<books.size();i++)
        {
            System.out.println(books.get(i));
        }
        return books;

    }

    public ArrayList<Book> getBooksByAuthor(String author)
    {
        ArrayList<Book> books=new ArrayList<>();
        books=bookDB.getBooksByAuthor(author);
        for(int i=0;i<books.size();i++)
        {
            System.out.println(books.get(i));
        }
        return books;
    }

    public ArrayList<Book> getBooksByGenre(String genre){
        ArrayList<Book> books=new ArrayList<>();
        books=bookDB.getBooksByGenre(genre);
        for(int i=0;i<books.size();i++)
        {
            System.out.println(books.get(i));
        }
        return books;
    }

    public boolean isBookBelongsToUser(int bookId, int lenderId) {
        return bookDB.bookBelongsToUser(bookId,lenderId);
    }
    public ArrayList<Book> getAllAvailableBooks() {
        ArrayList<Book> books = new ArrayList<>();
        books = bookDB.getAvailableBooks();
        return books;
    }

    public void decreaseQuantity(int bookId) {

        bookDB.decreaseBookQuantity(bookId);
    }
}
