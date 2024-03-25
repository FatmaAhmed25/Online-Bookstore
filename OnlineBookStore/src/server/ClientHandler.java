package server;

import server.BLL.*;
import server.model.Book;
import server.model.Request;
import server.model.User;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final UserService userService;
    private final RequestService requestService;
    private final DatabaseService db;
    private final BookService bookService;
    private final ChatService chatService;
   // public static String clientUsername;
    public static User loggedInUser;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.requestService =new RequestService();
        this.db = new DatabaseService();
        this.userService = new UserService();
        this.bookService=new BookService();
        this.chatService= new ChatService();
    }
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            boolean exit=false;
            while(!exit){
            // Read the request from the client
            String request = reader.readLine();
            if(request=="exit")
                exit=true;
            System.out.println(request);
            // Split the data string using ":" as the delimiter
            String[] parts = request.split(":");
            // Handle login or registration based on the request
            switch (parts[0]) {
                case "login":
                    handleLogin(writer,request);
                    break;
                case "register":
                    handleRegistration(writer,request);
                    break;
                case "add":
                    System.out.println("hiii");
                    handleAddBook(writer,request);
                    break;
                case "remove":
                    handleRemoveBook( writer,request);
                    break;
                case "search":
                    handleGetBooks( writer,request);
                    //handleGettBooks(writer,request);
                    break;
                case "borrow":
                    handleBorrowRequest(writer, request);
                    break;
                case "chat":
                    handleChatOptions(writer, request);
                    break;

                default:
                    writer.println("Invalid request");
                    break;
            }
        } }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    private void handleChatOptions(PrintWriter writer, String request) throws SQLException {
        String[] parts = request.split(":");
        String option = parts[1];
        if(option.equals("open"))
        {
            handleOpenChat(writer,request);
        }
    }
    private void handleOpenChat(PrintWriter writer, String request) throws SQLException {
//        String[] parts = request.split(":");
//        String recieverUsername = parts[1];
//        int recieverID = userService.getUser(recieverUsername).getId();
        int senderID=loggedInUser.getId();
        List<Request> requests = requestService.getAcceptedRequestsForRequester(senderID);
        String res = "";
        for (Request r : requests) {
            res += r;
            res += "\n";
        }
        res += "end";
        System.out.println(res);
        writer.println(res);
    }

    private void handleLogin(PrintWriter writer,String data) throws IOException {
        try {
            // Read username and password from the client
            String[] parts = data.split(":");
            String username = parts[1];
            String password = parts[2];
            //clientUsername=username;
            // Authenticate the user
            // Check if the user exists
            User user = userService.getUser(username);
            if (user == null) {
                // Send 404 Not Found response if the username is not found
                writer.println("404 Not Found: Username not found");
                return;
            }

            // Check if the password is correct
            if (!userService.authenticateUser(username, password)) {

                writer.println("401 Unauthorized: Wrong password");
                return;
            }

            writer.println("200 Login successful");
            loggedInUser=user;
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred during login");
        }
    }

    private void handleRegistration(PrintWriter writer,String data) throws IOException {
        try {
            // Read user details from the client
            String[] parts = data.split(":");
            String name = parts[1];
            String username = parts[2];
            String password = parts[3];

            System.out.println("Received registration request for: Name=" + name + ", Username=" + username);

            // Check if username is already reserved
            if (userService.userExists(username)) {
                writer.println("Username '" + username + "' is already reserved. Please choose another username.");
                return;
            }

            // Register the user
            userService.registerUser(name, username, password);
            System.out.println("User registered successfully: Username=" + username);

            // Send registration success message to the client
            writer.println("Registration successful");
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred during registration");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void handleAddBook(PrintWriter writer,String data) throws IOException {
        try {
            // Read the data string from the client
            //String data = reader.readLine();
            System.out.println(data);

            // Split the data string using ":" as the delimiter
            String[] parts = data.split(":");

            // Check if the data contains all necessary parts
            if (parts.length == 7)
            {
                String title = parts[1];
                System.out.println(title);
                String author = parts[2];
                System.out.println(author);
                String genre = parts[3];
                System.out.println(genre);
                double price = Double.parseDouble(parts[4]);
                System.out.println(price);
                String description = parts[5];
                System.out.println(description);
                int quantity = Integer.parseInt(parts[6]);
                System.out.println(quantity);


                // Fetch userID from userService based on the username
                //System.out.println(username);
                int userID = userService.getUser(loggedInUser.getUsername()).getId();

                // Create a Book object
                Book book = new Book(title, author, genre, price, description, userID, quantity);

                // Call the BookService method to add the book
                bookService.addBook(book);

                // Send success response to the client
                writer.println("Book added successfully");
            } else {
                // If the data does not contain all necessary parts, send an error response to the client
                writer.println("Error: Incomplete data received");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred while adding the book");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //check if the book belongs to the loggedin user
    private void handleRemoveBook(PrintWriter writer,String data) throws IOException {
        // Read book title from the client
        String[] parts = data.split(":");
        int bookId = Integer.parseInt(parts[1]);

        // Call the DatabaseService method to remove the book
        // Modify the DatabaseService class to include a method for removing books
        if(bookService.removeBook(bookId,loggedInUser.getId())){
            writer.println("Book removed successfully");
        }
        else {
            writer.println("Authorization error");
        }
    }
    private void handleGetBooks(PrintWriter writer,String data) throws IOException {
        // Read book title from the client
        String[] parts = data.split(":");
        String category=parts[1];
        String searchKey=parts[2];
        ArrayList<Book> books = null;
        if(Objects.equals(category, "title")) {
            books = bookService.getBooksByTitle(searchKey);
        }
        else if (Objects.equals(category, "author"))
        {
            books = bookService.getBooksByAuthor(searchKey);
        }
        else if(Objects.equals(category, "genre"))
        {
            books = bookService.getBooksByGenre(searchKey);
        }
        String res = "";
        assert books != null;
        for (Book book : books) {
            res += book;
            res += "\n";
        }
        res += "end";
        writer.println(res);
    }
    private void handleBorrowRequest(PrintWriter writer, String data) throws IOException {
        try {
            // Parse request data
            String[] parts = data.split(":");
            int bookId = Integer.parseInt(parts[1]);
            int lenderId =bookService.getBookByID(bookId).getOwnerID();

            // Check if the book belongs to the lender
            boolean authorized = bookService.isBookBelongsToUser(bookId, lenderId);
            if (!authorized) {
                writer.println("401 Unauthorized: You are not authorized to borrow this book");
                return;
            }

            // Add the borrowing request to the database
            Request request=new Request(loggedInUser.getId(),lenderId,bookId);
            requestService.addRequest(request);

            writer.println("Borrowing request sent successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred while processing borrowing request");
        }
    }
}