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

            // Handle login or registration based on user action
            if (action.equalsIgnoreCase("login") ) {
                // Prompt user for username and password

                System.out.println("Enter username:");
                String username = consoleInput.readLine();
                System.out.println("Enter password:");
                String password = consoleInput.readLine();

                // Send username and password to server
                //writer.println(username + ";" + password);
                // Send username to server
                writer.println(username);
                // Send password to server
                writer.println(password);

                System.out.println("hiiii");
                // Receive response from server
                String response = reader.readLine();
                System.out.println("herew");
                System.out.println("Server response: " + response);
            }
            else if (action.equalsIgnoreCase("register")){
                // Prompt user for username and password
                System.out.println("Enter name:");
                String name = consoleInput.readLine();
                System.out.println("Enter username:");
                String username = consoleInput.readLine();
                System.out.println("Enter password:");
                String password = consoleInput.readLine();

                // Send username and password to server
                //writer.println(username + ";" + password);
                // Send username to server
                writer.println(name);
                writer.println(username);
                // Send password to server
                writer.println(password);

                System.out.println("hiiii");
                // Receive response from server
                String response = reader.readLine();
                System.out.println("heree");
                System.out.println("Server response: " + response);

            } else {
                System.out.println("Invalid action. Please enter 'login' or 'register'.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
