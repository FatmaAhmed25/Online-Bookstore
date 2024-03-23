package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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

            // Handle login or registration based on user action
            if (action.equalsIgnoreCase("login")) {
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
                    loggedInActions(consoleInput,username);
                }
            } else if (action.equalsIgnoreCase("register")) {
                // Prompt user for username and password
                System.out.println("Enter name:");
                String name = consoleInput.readLine();
                System.out.println("Enter username:");
                String username = consoleInput.readLine();
                System.out.println("Enter password:");
                String password = consoleInput.readLine();
                // Send username to server
                writer.println(name);
                writer.println(username);
                // Send password to server
                writer.println(password);
                // Receive response from server
                String response = reader.readLine();
                System.out.println("Server response: " + response);

            } else {
                System.out.println("Invalid action. Please enter 'login' or 'register'.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loggedInActions(BufferedReader consoleInput,String username) {
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
                    addBookToStore(consoleInput,username);
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

    public static void addBookToStore(BufferedReader consoleInput,String username) {
        try {
            System.out.println("Enter book details:");
            System.out.print("Title: ");
            String title = consoleInput.readLine();
            System.out.print("Author: ");
            String author = consoleInput.readLine();
            System.out.print("Genre: ");
            String genre = consoleInput.readLine();
            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(consoleInput.readLine());
            System.out.print("Price: ");
            double price = Double.parseDouble(consoleInput.readLine());
            System.out.println("Book added to the store: " + title + " by " + author + " ,Genre is " + genre);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}