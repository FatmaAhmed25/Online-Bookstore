package server;

import server.BLL.DatabaseService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class BookStoreServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        DatabaseService.createTables();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Spawn a new thread to handle client communication
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
