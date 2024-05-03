# Online Bookstore Application
This is an online bookstore application built using Java socket programming and multithreading, 
implementing a client-server architecture. The application allows users to browse, search, borrow, 
lend books, and interact with each other.

## Requirements

- **Java 19 or above**
- **MySQL database**
- **IntelliJ IDEA**

## How to Run the Project

### Database Configuration

1. Create a MySQL database named "bookstore".
2. Navigate to `Online-Bookstore\OnlineBookStore\src\server\config`.
3. Add database connection configurations (port, URL, username, password) in the appropriate configuration file.

### Running the Server

1. Open the project in IntelliJ IDEA.
2. Run the server component using the IDE.

### Running the Client

1. Open a terminal.
2. Navigate to `OnlineBookStore\src\client\`.
3. Run the client component using the following command: `java BookStoreClient.java`.
4. Repeat the above steps to run multiple client instances in different terminals.

## Features

1. **Server-Client Communication:**
   - Utilizes Java SE sockets for seamless communication between the server and clients.

2. **Book Inventory Management:**
   - Manages a database of available books with detailed information.

3. **User Authentication:**
   - Implements secure login and registration mechanisms for user access.

4. **Browse and Search Books:**
   - Allows users to explore the bookstore's catalog and search for specific books.

5. **Add and Remove Books:**
   - Enables users to add or remove books they wish to lend.

6. **Submit a Request:**
   - Facilitates borrowing requests between users, initiating chat functionality.

7. **Accept / Reject a Request:**
   - Allows users to manage incoming borrowing requests.

8. **Request History:**
   - Provides access to users' request history and their statuses.

9. **Library Overall Statistics:**
   - Admins can view the overall library status, including borrowed and available books, and request statuses.

10. **Error Handling:**
    - Implements mechanisms to handle various error scenarios.
