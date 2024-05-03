package server;

import server.BLL.*;
import server.model.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

import static server.BookStoreServer.clients;
import static server.BookStoreServer.onlineUsers;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final UserService userService;
    private final RequestService requestService;
    private final DatabaseService db;
    private final BookService bookService;
    private final ChatService chatService;
    // public static String clientUsername;
    public User loggedInUser;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.requestService = new RequestService();
        this.db = new DatabaseService();
        this.userService = new UserService();
        this.bookService = new BookService();
        this.chatService = new ChatService();
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            boolean exit = false;
            while (!exit) {
                // Read the request from the client
                String request = reader.readLine();
                if (Objects.equals(request, "exit"))
                    exit = true;
                System.out.println(request);
                // Split the data string using ":" as the delimiter
                String[] parts = request.split(":");
                // Handle login or registration based on the request
                switch (parts[0]) {
                    case "login":
                        handleLogin(writer, request);
                        break;
                    case "register":
                        handleRegistration(writer, request);
                        break;
                    case "add":
                        handleAddBook(writer, request);
                        break;
                    case "remove":
                        handleRemoveBook(writer, request);
                        break;
                    case "search":
                        handleGetBooks(writer, request);
                        //handleGetBooks(writer,request);
                        break;
                    case "borrow":
                        handleBorrowRequest(writer, request);
                        break;
                    case "chat":
                        handleChatOptions(writer, request);
                        break;
                    case "getRequests":
                        handleGetRequests(writer);
                        break;
                    case "request":
                        handleRequest(writer, request);
                        break;
                    case "viewborrowrequests":
                        viewBorrowerRequests(writer);
                        break;
                    case "viewlenderequests":
                        viewLenderRequests(writer);
                        break;
                    case "browse":
                        handleBrowse(writer);
                        break;

                    case "statistics":
                        handleStatisticsRequest(writer, request);
                        break;

                    default:
                        writer.println("Invalid request");
                        break;
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleStatisticsRequest(PrintWriter writer, String request) throws IOException {
        try {
            // Parse the request to determine which statistics data the client is requesting
            String[] parts = request.split(":");
            String statisticsType = parts[1];

            // Retrieve the relevant statistics data based on the type requested
            String res = "";
            switch (statisticsType) {

                case "overall":
                    //getOverall();
                    break;
                case "availablebooks":
                    List<Book>books=bookService.getAvailableBooks();
                    assert books != null;
                    for (Book b : books) {
                        res += b;
                        res += "\n";
                    }
                    writer.println(res);

                    break;
                case "borrowedbooks":
                    List<Book>borrowedBooks=bookService.getCurrentBorrowedBooks();
                    assert borrowedBooks!= null;
                    for (Book b : borrowedBooks) {
                        res += b;
                        res += "\n";
                    }
                    writer.println(res);

                    break;
                case "acceptedrequests":
                    List<Request>acceptRequests=requestService.getAcceptedRequests();
                    assert acceptRequests!=null;
                    for (Request r : acceptRequests) {
                        res += r;
                        res += "\n";
                    }
                    writer.println(res);
                    break;

                case "rejectedrequests":
                    List<Request>rejectedRequests=requestService.getRejectedRequests();
                    assert rejectedRequests!=null;
                    for (Request r : rejectedRequests) {
                        res += r;
                        res += "\n";
                    }
                    writer.println(res);
                    break;
                case "pendingrequests":
                    List<Request>pendingRequests=requestService.getPendingRequests();
                    assert pendingRequests!=null;
                    for (Request r : pendingRequests) {
                        res += r;
                        res += "\n";
                    }
                    writer.println(res);
                    break;
                default:
                    writer.println("Invalid statistics type");
                        return;
            }

            // Send the statistics data back to the client
           // writer.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("Error occurred while processing statistics request");
        }
    }

    private void viewLenderRequests(PrintWriter writer) {
        System.out.println(loggedInUser.getId());
        List<Request>requests=requestService.getRequestsForLender(loggedInUser.getId());
        String res = "";
        assert requests != null;
        for (Request r : requests) {
            res += r;
            res += "\n";
        }
        writer.println(res);


    }

    private void viewBorrowerRequests(PrintWriter writer) {
        System.out.println(loggedInUser.getId());
        List<Request>requests=requestService.getRequestsForBorrower(loggedInUser.getId());
        String res = "";
        assert requests != null;
        for (Request r : requests) {
            res += r;
            res += "\n";
        }
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

            BookStoreServer.onlineUsers.put(username, this);
            writer.println("200 Login successful");
            addToClients(user.getId(),writer);
            loggedInUser=user;

        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred during login");
        }
    }


    private void handleBrowse(PrintWriter writer) {
        List<Book> books = bookService.getAllAvailableBooks();
        String res = "";
        assert books != null;
        for (Book b : books) {
            res += b;
            res += "\n";
        }
        writer.println(res);
    }

    private void handleRequest(PrintWriter writer, String request) throws SQLException {
        String[] parts = request.split(":");
        int requestId= Integer.parseInt(parts[2]);
        if(parts[1].equals("accept"))
        {
            requestService.acceptRequest(requestId);
            writer.println("Request Accepted successfully");
            Request borrowRequest= requestService.getRequest(requestId);
            int requester_id=borrowRequest.getBorrowerId();
            bookService.decreaseQuantity(borrowRequest.getBookId());
            User requester=userService.getUserById(requester_id);
            String requesterUsername= requester.getUsername();
            //create a chat room for requester and lender
            chatService.createChatRoom(requester_id,loggedInUser.getId(),requesterUsername,loggedInUser.getUsername());
        }
        else if(parts[1].equals("reject")) {
            requestService.rejectRequest(requestId);
            writer.println("Request Rejected successfully");

        };
    }

    private void handleGetRequests(PrintWriter writer) {
        List<Request>requests=requestService.getPendingRequestsForLender(loggedInUser.getId());
        if (requests.isEmpty())
        {
            writer.println("No pending requests");
            return;
        }
        else{
        String res = "";
       // assert requests != null;
        for (Request r : requests) {
            res += r;
            res += "\n";
        }
        writer.println(res);
    }
    }

    private void handleChatOptions(PrintWriter writer, String request) throws SQLException {
        String[] parts = request.split(":");
        String option = parts[1];
        if (option.equals("open")) {
            handleOpenChat(writer, request);
        } else if (option.equals("start")) {
            handleStartChat(writer, request);
        } else if (option.equals("send")) {
            handleSendMessage(writer, request);
        }
    }

    private void handleStartChat(PrintWriter writer, String request) throws SQLException {
        String[] parts = request.split(":");
        String recieverUsername = parts[2];
        if (!validChats(recieverUsername)) {
            writer.println("Unauthorized chat");
            return;
        }
        User recieverUser = userService.getUser(recieverUsername);
        if (recieverUser == null) {
            writer.println("Reciever User in not found");
//            throw new NullPointerException("Receiver User is not found");
            return;
        }
        int recieverId = recieverUser.getId();
        chatService.createChatRoom(loggedInUser.getId(), recieverId, recieverUsername, loggedInUser.getUsername());
        ChatRoom room = chatService.getChatRoomByRequesterIdAndBorrowerId(loggedInUser.getId(), recieverId);
        List<Message> messages = chatService.getMessagesByChatRoom(room.getChatRoomId());
        String res = "";
        for (Message r : messages) {
            res += r;
            res += "\n";
        }
        System.out.println(res);
        writer.println(res);

    }


    private boolean validChats(String recieverUsername) {
        return chatService.isValidChat(loggedInUser.getUsername(), recieverUsername);

    }

    private void handleSendMessage(PrintWriter writer, String request) throws SQLException {
        String[] parts = request.split(":");
        String recieverUsername = parts[2];
        User recieverUser = userService.getUser(recieverUsername);
        if (recieverUser == null) {
            writer.println("Reciever User in not found");
//            throw new NullPointerException("Receiver User is not found");
            return;
        }
        if (recieverUsername.equals(loggedInUser.getUsername())) {
            writer.println("You can't chat with yourself");
            return;
        }
        if (!validChats(recieverUsername)) {
            writer.println("Unauthorized chat");
            return;
        }
        int recieverID = recieverUser.getId();
        ChatRoom room = chatService.getChatRoomByRequesterIdAndBorrowerId(loggedInUser.getId(), recieverID);
        String message = parts[3];
        if (message.equals("x")) {
            writer.println("You left the chat");
            return;
        }
        assert room != null;
        chatService.createMessage(room.getChatRoomId(), loggedInUser.getUsername(), recieverUsername, message);
        sendMessage(recieverID, message);

    }
    public void sendMessage(int clientId, String message) {
        PrintWriter writer = clients.get(clientId);
        if (writer != null) {
            writer.println("Message from "+loggedInUser.getUsername()+": "+message);
        } else {
            System.err.println("Client ID " + clientId + " not found.");
        }
    }

    private void handleOpenChat(PrintWriter writer, String request) throws SQLException {
        List<ChatRoom> chatRooms = chatService.getAllChatRoomsFoUser(loggedInUser.getId());
        String res = "";
        for (ChatRoom r : chatRooms) {
            res += r;
            res += "\n";
        }
        System.out.println(res);
        writer.println(res);
    }
    private void addToClients(int clientId, PrintWriter writer)
    {
        clients.put(clientId, writer);
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
            writer.println("200 Registration successful");
            User user = userService.getUser(username);
            loggedInUser=user;
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
        else{
            writer.println("Category is not found");
            return;
        }
        String res = "";
        assert books != null;
        for (Book book : books) {
            res += book;
        }
        writer.println(res);
    }
    private void handleBorrowRequest(PrintWriter writer, String data) throws IOException {
        try {
            // Parse request data
            String[] parts = data.split(":");
            int bookId = Integer.parseInt(parts[1]);
            Book book =bookService.getBookByID(bookId);
            if (book==null)
                writer.println("No book with this ID");

            {
            int lenderId =book.getOwnerID();
            if(book.getQuantity()==0)
            {
                writer.println("Cant lend this book because its quantity is zero");
                return;
            }
            if(lenderId == loggedInUser.getId())
            {
                writer.println("Error: You cant lend your own book");
                return;
            }
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
        }
        }
        catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred while processing borrowing request");
        }
    }
}