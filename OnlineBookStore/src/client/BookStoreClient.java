package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BookStoreClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    public static String username1;
    public static boolean isAdmin;
    public static BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    static Socket socket;

    static {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static BufferedReader reader;

    static {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

    public BookStoreClient() throws IOException {
    }

    public static void main(String[] args) {
        try
        {

            System.out.println("Connected to server: " + SERVER_HOST + ":" + SERVER_PORT);

            // Prompt user for action (login or register)

            //while(true){
                System.out.println("Enter 'login' or 'register':");
                String action = consoleInput.readLine();
                switch (action.toLowerCase()) {
                    case "login":
                        handleLogin(consoleInput, writer, reader);


                        break;
                    case "register":
                        handleRegistration(consoleInput, writer, reader);

                        break;

                    default:
                        System.out.println("Invalid action. Please enter 'login', 'register'");
                        break;
          // }
        } }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void recieveFromServer(){
        Runnable serverListner = () -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("\n>> " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread messageReceiverThread = new Thread(serverListner);
        messageReceiverThread.start();
    }
    public static void loggedInActions() {
        Runnable loggedInTask = () -> {
            try {
                boolean loggedIn = true;
                while (loggedIn) {
                    Thread.sleep(600);
                    // Sub-menu options after logging in
                    System.out.println("Actions:");
                    System.out.println("1. Add book to store");
                    System.out.println("2. Remove book from store");
                    System.out.println("3. Search for a book");
                    System.out.println("4. Borrow Request");
                    System.out.println("5. Open chat");
                    System.out.println("6. Accept/Reject borrowing requests");
                    System.out.println("7. view borrowing requests");
                    System.out.println("8. view lending requests");
                    System.out.println("9. Browse all books");
                    System.out.println("10. Logout");
                    System.out.print("Choose an action: ");
                    int loggedInChoice = Integer.parseInt(consoleInput.readLine());

                    switch (loggedInChoice) {
                        case 1:
                            handleAddBook(consoleInput, writer, reader);
                            break;
                        case 2:
                            handleRemoveBook(consoleInput, writer, reader);
                            break;
                        case 3:
                            searchActions();
                            break;
                        case 4:
                            handleBorrowRequest(writer, reader);
                            break;
                        case 5:
                            handleChat(writer, reader);
                            break;
                        case 6:
                            requestsMenu(writer,reader);
                            break;
                        case 7:
                            viewMyRequestsAsBorrower(writer);
                            break;
                        case 8:
                            viewMyRequestsAsLender(writer);
                            break;
                        case 9:
                            handleBrowse(writer);
                            break;

                        case 10:
                            loggedIn = false;
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                // After logging out, prompt for login or register
                System.out.println("Do you want to login or register?");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(consoleInput.readLine());

                switch (choice) {
                    case 1:
                        handleLogin(consoleInput, writer, reader);
                        break;
                    case 2:
                        handleRegistration(consoleInput, writer, reader);
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Exiting.");
                        break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        // Start a new thread for loggedInTask
        Thread loggedInThread = new Thread(loggedInTask);
        loggedInThread.start();
    }

    private static void statisticsMenu()
    {

        Runnable loggedInTask = () -> {
            boolean loggedIn = true;
            while (loggedIn)
            {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("\nStatistics Menu:");
                System.out.println("1. Overall Statistics");
                System.out.println("2. Available Books");
                System.out.println("3. Current Borrowed Books");
                System.out.println("4. Accepted Requests");
                System.out.println("5. Pending Requests");
                System.out.println("6. Rejected Requests");
                System.out.println("7. Logout");

                System.out.print("Choose an option: ");
                int choice = 0;
                try
                {
                    choice = Integer.parseInt(consoleInput.readLine());
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                switch (choice)
                {
                    case 1:
                        writer.println("statistics:availablebooks");
                        writer.println("statistics:borrowedbooks");
                        writer.println("statistics:acceptedrequests");
                        writer.println("statistics:rejectedrequests");
                        writer.println("statistics:pendingrequests");

                        break;
                    case 2:
                        writer.println("statistics:availablebooks");
                        System.out.println("Available Books Statistics:");

                        break;
                    case 3:
                        writer.println("statistics:borrowedbooks");
                        System.out.println("Current Borrowed Books Statistics:");

                        break;
                    case 4:
                        writer.println("statistics:acceptedrequests");
                        System.out.println("Accepted Requests Statistics:");
                        break;
                    case 5:
                        writer.println("statistics:rejectedrequests");
                        System.out.println("Rejected Requests Statistics:");
                        break;
                    case 6:
                        writer.println("statistics:pendingrequests");
                        System.out.println("pending Requests Statistics:");
                        break;
                    case 7:
                        loggedIn = false;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
            };
        // Start a new thread for loggedInTask
        Thread loggedInThread = new Thread(loggedInTask);
        loggedInThread.start();
    }
    private static void viewMyRequestsAsBorrower(PrintWriter writer) throws IOException {
        // Send a request to the server to retrieve the borrower's requests
        writer.println("viewborrowrequests");

    }
    private static void viewMyRequestsAsLender(PrintWriter writer) throws IOException {
        // Send a request to the server to retrieve the borrower's requests
        writer.println("viewlenderequests");

    }
    private static void handleLogin(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException, InterruptedException {
        // Prompt user for username and password
        System.out.println("Enter username:");
        String username = consoleInput.readLine();

        username1=username;
        System.out.println("Enter password:");
        String password = consoleInput.readLine();
        writer.println("login"+":"+username+":"+password);

        String response = reader.readLine();
        System.out.println("Server response: " + response);
        if (response.contains("2")) {
            // Check if the user is an admin
            if (username.equals("admin"))
            {
                System.out.println("You are now logged in as admin.");
                recieveFromServer();
                statisticsMenu();


                // isAdmin = true;
            }
            else
            {
                System.out.println("You are now logged in as a regular user.");
                recieveFromServer();
                //isAdmin = false;
                loggedInActions();
            }

        }
        else {
            System.out.println("Login failed. Please try again.");

        }
    }
    private static void handleRegistration(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for registration details
        System.out.println("Enter name:");
        String name = consoleInput.readLine();
        System.out.println("Enter username:");
        String username = consoleInput.readLine();
        username1=username;
        System.out.println("Enter password:");
        String password = consoleInput.readLine();
        writer.println("register"+":"+name+":"+username+":"+password);
        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
        if (response.contains("2"))
        {
        loggedInActions();
        }
    }



    private static void handleSearchBooks(PrintWriter writer1,BufferedReader reader1,String category,String res) throws IOException {
        writer1.println("search"+":"+category+":"+res);
    }
    private static void handleBrowse(PrintWriter writer) {
        writer.println("browse");
    }

    public static void searchActions() throws IOException {
        System.out.println("Search by:");
        System.out.println("1. Search by title");
        System.out.println("2. Search by author");
        System.out.println("3. Search by genre");
        System.out.println("4. Exit");
        System.out.print("Choose an action: ");
        int searchChoice = Integer.parseInt(consoleInput.readLine());
        String res="";
        switch (searchChoice) {
            case 1:
                System.out.println("Enter the title that you want to search for: ");
                res= consoleInput.readLine();
                handleSearchBooks(writer,reader,"title",res);
                break;
            case 2:
                System.out.println("Enter the author that you want to search for: ");
                res= consoleInput.readLine();

                handleSearchBooks(writer,reader,"author",res);
                break;
            case 3:
                System.out.println("Enter the genre that you want to search for: ");
                res= consoleInput.readLine();
                handleSearchBooks(writer,reader,"genre",res);

                break;

        }
    }

    private static void handleChat(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("chat:open");
        System.out.println("Enter username that you want to chat with: ");
        String recieverUsername = consoleInput.readLine();
        if (recieverUsername.equals(username1))
            System.out.println("You can't chat with yourself");
        else {
            String message = "";
            writer.println("chat:start:" + recieverUsername);
            while (!message.equals("x")) {
                System.out.print(">>>");
                message = consoleInput.readLine();

                if (!message.equals("x")) {
                    writer.println("chat:send:" + recieverUsername + ":" + message);
                }
            }
        }
    }





    private static void handleAddBook(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for book details
        System.out.println("Enter book title:");
        String title = consoleInput.readLine();
        System.out.println("Enter author:");
        String author = consoleInput.readLine();
        System.out.println("Enter genre:");
        String genre = consoleInput.readLine();
        System.out.println("Enter price:");
        double price = Double.parseDouble(consoleInput.readLine());
        System.out.println("Enter description:");
        String description = consoleInput.readLine();
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(consoleInput.readLine());

        writer.println("add"+":"+title+":"+author+":"+genre+":"+price+":"+description+":"+quantity);

    }
private static void handleRemoveBook(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for book ID
        System.out.println("Enter book ID to remove:");
        int bookId = Integer.parseInt(consoleInput.readLine());
        // Send book ID to server
        writer.println("remove"+":"+bookId);

        }


    private static void handleBorrowRequest(PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for book ID and lender's username
        System.out.println("Enter book ID you want to borrow:");
        int bookId = Integer.parseInt(consoleInput.readLine());

        // Send borrowing request to server
        writer.println("borrow:" + bookId );
    }
    private static void requestsMenu(PrintWriter writer, BufferedReader reader) throws IOException {
        {
            writer.println("getRequests:"+username1);
            System.out.println("Enter the ID of the request:");
            int requestId = Integer.parseInt(consoleInput.readLine());
            System.out.println("1. Accept");
            System.out.println("2. Reject");
            System.out.println("Enter your choice");
            String choice = consoleInput.readLine();
            if(choice.equals("1"))
            {
                writer.println("request:accept:"+requestId);
                //System.out.println("You are now chatting with client: ");
            } else if (choice.equals("2")) {
                writer.println("request:reject:"+requestId);
            }

        }
    }



}

