package server.DAL;

import server.BLL.DatabaseService;
import server.model.ChatRoom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomDAL {
    public void saveChatRoom(int requesterId, int borrwerId,String requesterUsername,String lenderUsername) throws SQLException {
        String query = "INSERT INTO ChatRooms (requester_id, lender_id,senderUsername,recipientUsername) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseService.getConnection().prepareStatement(query)) {
            statement.setInt(1, requesterId);
            statement.setInt(2, borrwerId);
            statement.setString(3,requesterUsername);
            statement.setString(4,lenderUsername);
            statement.executeUpdate();
        }
    }
    public List<ChatRoom> getChatRoomsForUser(int userId) {
        List<ChatRoom> chatRooms = new ArrayList<>();

        String sql = "SELECT * FROM ChatRooms WHERE requester_id = ? OR lender_id = ?";

        try (PreparedStatement statement = DatabaseService.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int chatRoomId = resultSet.getInt("id");
                    int requesterId = resultSet.getInt("requester_id");
                    int lenderId = resultSet.getInt("lender_id");
                    String requesterUsername = resultSet.getString("senderUsername");
                    String lenderUsername = resultSet.getString("recipientUsername");
                    ChatRoom chatRoom = new ChatRoom(chatRoomId, requesterId, lenderId,requesterUsername,lenderUsername);
                    chatRooms.add(chatRoom);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatRooms;
    }

    public ChatRoom getChatRoomByRequesterIdAndBorrowerId(int requesterId, int lenderId) {
        ChatRoom chatRoom = null;
        String sql = "SELECT * FROM ChatRooms WHERE (requester_id = ? AND lender_id = ?) OR (lender_id = ? AND requester_id = ?)";
        try (PreparedStatement statement = DatabaseService.getConnection().prepareStatement(sql)) {
            statement.setInt(1, requesterId);
            statement.setInt(2, lenderId);
            statement.setInt(3, lenderId); // Set the third parameter
            statement.setInt(4, requesterId);    // Set the fourth parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) { // Check if there's a result
                    int chatRoomId = resultSet.getInt("id");
                    String requesterUsername = resultSet.getString("senderUsername");
                    String lenderUsername = resultSet.getString("recipientUsername");
                    chatRoom = new ChatRoom(chatRoomId, requesterId, lenderId, requesterUsername, lenderUsername);
                } else {
                    // Handle case when no chat room is found
                    System.out.println("No chat room found for the given IDs.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatRoom;

    }
}
