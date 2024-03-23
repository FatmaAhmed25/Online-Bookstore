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

            // Prompt user for action (login, register, add, or remove)
            System.out.println("Enter 'login', 'register', 'add', or 'remove':");
            String action = consoleInput.readLine();
            writer.println(action);

            // Handle login, registration, adding, or removing based on user action
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

    private static void handleLogin(BufferedReader consoleInput, PrintWriter writer, BufferedReader reader) throws IOException {
        // Prompt user for username and password
        System.out.println("Enter username:");
        String username = consoleInput.readLine();
        System.out.println("Enter password:");
        String password = consoleInput.readLine();

        // Send username and password to server

        writer.println(username);
        writer.println(password);

        // Receive response from server
        String response = reader.readLine();
        System.out.println("Server response: " + response);
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

        // Send book details to server
        writer.println(title);
        writer.println(author);
        writer.println(genre);
        writer.println(price);
        writer.println(description);

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