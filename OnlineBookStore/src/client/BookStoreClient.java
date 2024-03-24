package client;

import java.io.*;
import java.net.Socket;

public class BookStoreClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server: " + SERVER_HOST + ":" + SERVER_PORT);

            // Prompt user for action (login or register)
            System.out.println("Enter 'login' or 'register':");
            String action = consoleInput.readLine();
            writer.println(action);

            switch (action.toLowerCase()) {
                case "login":
                    handleLogin(consoleInput, writer, reader);
                    break;
                case "register":
                    handleRegistration(consoleInput, writer, reader);
                    break;
                case "add":
                    handleAddBook(consoleInput, writer, reader);
                    break;
                case "remove":
                    handleRemoveBook(consoleInput, writer, reader);
                    break;
                default:
                    System.out.println("Invalid action. Please enter 'login', 'register', 'add', or 'remove'.");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loggedInActions(BufferedReader consoleInput, String username) {
        try {
            // Sub-menu options after logging in
            System.out.println("Actions:");
            System.out.println("1. Add book to store");
            System.out.println("2. Search for a book");
            System.out.println("3. See my request history");
            System.out.println("4. Open messages");
            System.out.println("5.Logout");
            System.out.print("Choose an action: ");
            int loggedInChoice = Integer.parseInt(consoleInput.readLine());

            switch (loggedInChoice) {
                case 1:
                    //handleAddBook(consoleInput, username,);
                    break;
                case 2:
                    // Implement search for a book functionality
                    break;
                case 3:
                    // Implement see my request history functionality
                    break;
                case 4:
                    // Implement open messages functionality
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleLogin(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for username and password
        System.out.println("Enter username:");
        String username = consoleInput.readLine();
        System.out.println("Enter password:");
        String password = consoleInput.readLine();
        // Send username to server
        writer.println(username);
        // Send password to server
        writer.println(password);
        String response = reader.readLine();
        System.out.println("Server response: " + response);
        if (response.contains("2")) {
            System.out.println("You are now logged in.");
            loggedInActions(consoleInput, username);
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

        // Send registration details to server
        writer.println(name);
        writer.println(username);
        writer.println(password);

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

        // Send book details to server
        writer.println(title);
        writer.println(author);
        writer.println(genre);
        writer.println(price);
        writer.println(description);
        writer.println(quantity);

        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }
private static void handleRemoveBook(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for book ID
        System.out.println("Enter book ID to remove:");
        int bookId = Integer.parseInt(consoleInput.readLine());

        // Send book ID to server
        writer.println(bookId);

        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
        }

}