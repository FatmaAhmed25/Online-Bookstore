package client;

import java.io.*;
import java.net.Socket;

public class BookStoreClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    public static String username1;

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

            while(true){
                System.out.println("Enter 'login' or 'register':");
                String action = consoleInput.readLine();
               // writer.println(action);


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
            }
        } }catch (IOException e) {
            e.printStackTrace();
        }
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
    public static void loggedInActions() {
        try {

            boolean loggedIn = true;
            while (loggedIn) {
            // Sub-menu options after logging in
            System.out.println("Actions:");
            System.out.println("1. Add book to store");
            System.out.println("2. Remove book from store");
            System.out.println("3. Search for a book");
            System.out.println("4. See my request history");
            System.out.println("5. Start chat");
            System.out.println("5. Open messages");
            System.out.println("6.Logout");
            System.out.print("Choose an action: ");
            int loggedInChoice = Integer.parseInt(consoleInput.readLine());

            switch (loggedInChoice) {
                case 1:
                    handleAddBook(consoleInput,writer,reader);
                    break;
                case 2:
                    handleRemoveBook(consoleInput,writer,reader);
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
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleChat(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("chat:open");
        String response;
        while (!(response = reader.readLine()).equals("end")) {
            System.out.println("Your Accepted Requests: " + response);
        }

//        System.out.println("Enter recipient username: ");
//        String recipient = consoleInput.readLine();
//        while (true) {
//
//            if (recipient.equalsIgnoreCase("exit")) {
//                break;
//            }
//            System.out.println("Enter message:");
//            String message = consoleInput.readLine();
//            sendMessage(recipient, message);
//
//        }

    }

    private static void handleLogin(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
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
            System.out.println("You are now logged in.");
            loggedInActions();
        }
    }

    private static void handleRegistration(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for registration details
        System.out.println("Enter name:");
        String name = consoleInput.readLine();
        System.out.println("Enter username:");
        String username = consoleInput.readLine();
        System.out.println("Enter password:");
        String password = consoleInput.readLine();
        writer.println("register"+":"+name+":"+username+":"+password);
        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
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

        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }
private static void handleRemoveBook(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for book ID
        System.out.println("Enter book ID to remove:");
        int bookId = Integer.parseInt(consoleInput.readLine());

        // Send book ID to server
        writer.println("remove"+":"+bookId);

        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
        }

    private static void handleSearchBooks(PrintWriter writer1,BufferedReader reader1,String category,String res) throws IOException {
        writer1.println("search"+":"+category+":"+res);
        // Receive response from server
        String response;
        while (!(response = reader.readLine()).equals("end")) {
            System.out.println("Server response: " + response);
        }
    }
    private static void handleBorrowRequest(PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for book ID and lender's username
        System.out.println("Enter book ID you want to borrow:");
        int bookId = Integer.parseInt(consoleInput.readLine());
        // Send borrowing request to server
        writer.println("borrow:" + bookId );

        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }



}

