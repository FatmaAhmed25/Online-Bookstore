package server.DAL;

import server.BLL.DatabaseService;
import server.model.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDAL {
    public void saveMessage(Message message) throws SQLException {
        String query = "INSERT INTO Messages (chatRoomId, senderUsername, recipientUsername, content, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseService.getConnection().prepareStatement(query)) {
            statement.setInt(1, message.getChatRoomId());
            statement.setString(2, message.getSenderUsername());
            statement.setString(3, message.getRecipientUsername());
            statement.setString(4, message.getContent());
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(message.getTimestamp()));
            statement.executeUpdate();
        }
    }
    public List<Message> getMessagesForChatRoom(int chatRoomId) {
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM Messages WHERE id = ? ORDER BY timestamp ASC"; // Sorting by timestamp in ascending order

        try (PreparedStatement statement = DatabaseService.getConnection().prepareStatement(sql)) {
            statement.setInt(1, chatRoomId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String senderUsername = resultSet.getString("senderUsername");
                    String recipientUsername = resultSet.getString("recipientUsername");
                    String content = resultSet.getString("content");
                    LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();

                    Message message = new Message(senderUsername, recipientUsername, content);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

}
