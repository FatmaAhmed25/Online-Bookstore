package server.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre; // can be enum
    private double price;
    private int quantity;
    private String description;
    private int ownerID;
    private List<User> lentBy;
    public String getTitle() {
        return title;
    }
    public Book(String title, String author, String genre, double price, String description,int ownerID,int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.ownerID = ownerID;
        this.lentBy=new ArrayList<>();
    }
    public Book(int id,String title, String author, String genre, double price, String description,int ownerID,int quantity) {
        this.id=id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.ownerID = ownerID;
        this.lentBy=new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public List<User> getLentBy() {
        return lentBy;
    }

    public void addUser(User user) {
        this.lentBy.add(user);
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwner(int ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public String toString() {
        return "Book{" +
                "book id"+id+'\''+
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", price='" + price +'\''+
                ", quantity='" + quantity +'\''+
                '}';
    }
}

