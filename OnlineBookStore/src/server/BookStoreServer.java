package server;

import server.BLL.DatabaseService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class BookStoreServer {
    private static final int PORT = 12345;
    public static Map<Integer, PrintWriter> clients = new HashMap<>();
    public static Map<String, ClientHandler> onlineUsers = new HashMap<>();
    public static void main(String[] args) {
        DatabaseService.createTables();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Spawn a new thread to handle client communication
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
