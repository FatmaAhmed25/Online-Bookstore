package server.model;

import java.util.List;

public class Book {
    private String title;
    private String author;
    private String genre; // can be enum
    private double price;
    private int quantity;
    private User owner;
    private List<User> lentBy;
    public String getTitle() {
        return title;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", price='" + price +'\''+
                ", quantity='" + quantity +'\''+
                ", users =" + lentBy +
                '}';
    }
}

