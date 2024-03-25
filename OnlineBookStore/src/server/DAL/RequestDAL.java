package server.DAL;

import server.BLL.DatabaseService;
import server.model.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestDAL {

    // Add a borrowing request to the database
    public  synchronized  void addRequest(Request request) throws SQLException {
        String addRequestSQL = "INSERT INTO Requests (borrower_id, lender_id, book_id, status) VALUES (?, ?, ?, 'pending')";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(addRequestSQL)) {
            pstmt.setInt(1, request.getBorrowerId());
            pstmt.setInt(2, request.getLenderId());
            pstmt.setInt(3, request.getBookId());


            pstmt.executeUpdate();
            System.out.println("Request added to the database.");
        } catch (SQLException e) {
            System.out.println("Error adding request to the database: " + e.getMessage());
            throw e;
        }
    }

    // Get pending borrowing requests for a specific user
    public  synchronized  List<Request> getPendingRequestsForUser(int userId) {
        List<Request> pendingRequests = new ArrayList<>();
        String getPendingRequestsSQL = "SELECT * FROM Requests WHERE lender_id = ? AND status = 'Pending'";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getPendingRequestsSQL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int borrowerId = rs.getInt("borrower_id");
                    int bookId = rs.getInt("book_id");
                    String status = rs.getString("status");

                    pendingRequests.add(new Request(borrowerId, userId, bookId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingRequests;
    }

    // Get all borrowing requests for a specific user
    public  synchronized  List<Request> getAllRequestsForUser(int userId) {
        List<Request> allRequests = new ArrayList<>();
        String getAllRequestsSQL = "SELECT * FROM Requests WHERE lender_id = ? OR borrower_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getAllRequestsSQL)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int borrowerId = rs.getInt("borrower_id");
                    int lenderId = rs.getInt("lender_id");
                    int bookId = rs.getInt("book_id");
                    String status = rs.getString("status");

                    allRequests.add(new Request(borrowerId, lenderId, bookId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allRequests;
    }

    // Accept a borrowing request
    public  synchronized  void acceptRequest(int requestId) {
        updateRequestStatus(requestId, "Accepted");
    }

    // Reject a borrowing request
    public  synchronized  void rejectRequest(int requestId) {
        updateRequestStatus(requestId, "Rejected");
    }

    private  synchronized  void updateRequestStatus(int requestId, String status) {
        String updateRequestStatusSQL = "UPDATE Requests SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateRequestStatusSQL)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);

            pstmt.executeUpdate();
            System.out.println("Request status updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
