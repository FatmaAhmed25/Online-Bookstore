package server;

import service.UserService;
import model.User;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final UserService userService;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read the request from the client
            String request = reader.readLine();

            // Handle login or registration based on the request
            switch (request) {
                case "login":
                    handleLogin(reader, writer);
                    break;
                case "register":
                    handleRegistration(reader, writer);
                    break;
                default:
                    writer.println("Invalid request");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(BufferedReader reader, PrintWriter writer) throws IOException {
        try {
            // Read username and password from the client
            String username = reader.readLine();
            String password = reader.readLine();

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

            writer.println("Login successful");
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error occurred during login");
        }
    }

    private void handleRegistration(BufferedReader reader, PrintWriter writer) throws IOException {
        try {
            // Read user details from the client
            String name = reader.readLine();
            String username = reader.readLine();
            String password = reader.readLine();

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
}