package server.model;

public class Request {
    private int id;
    private int borrowerId;
    private int lenderId;
    private int bookId;
    private String status; // Pending, Accepted, Rejected

    // Constructors
    public Request() {
    }

    public Request(int borrowerId, int lenderId, int bookId) {
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.bookId = bookId;

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", borrowerId=" + borrowerId +
                ", lenderId=" + lenderId +
                ", bookId=" + bookId +
                ", status='" + status + '\'' +
                '}';
    }
}
