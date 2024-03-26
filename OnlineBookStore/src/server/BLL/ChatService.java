package server.BLL;

import server.DAL.ChatRoomDAL;
import server.DAL.MessageDAL;
import server.model.ChatRoom;
import server.model.Message;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatService {
    ChatRoomDAL chatRoomDB= new ChatRoomDAL();
    MessageDAL messageDB= new MessageDAL();

    public void createChatRoom(int requesterId, int borrowerId,String requesterUsername, String lenderUsername) throws SQLException {
        if(getChatRoomByRequesterIdAndBorrowerId(requesterId,borrowerId)==null)
        {
            chatRoomDB.saveChatRoom(requesterId,borrowerId,requesterUsername,lenderUsername);
        }
    }

    public Message createMessage(int chatRoomId, String senderUsername, String recipientUsername, String content) throws SQLException {
        LocalDateTime timeStamp= LocalDateTime.now();
        Message message = new Message(chatRoomId, senderUsername,recipientUsername,content,timeStamp);
        messageDB.saveMessage(message);
        return message;
    }
    public ChatRoom getChatRoomByRequesterIdAndBorrowerId(int requesterId, int lenderId){
        ChatRoom room=chatRoomDB.getChatRoomByRequesterIdAndBorrowerId(requesterId,lenderId);
        return room;
    }
    public List<ChatRoom> getAllChatRoomsFoUser(int userId)
    {
        List<ChatRoom> rooms =chatRoomDB.getChatRoomsForUser(userId);
        return rooms;
    }


    public List<Message> getMessagesByChatRoom(int chatRoomId) {
        return messageDB.getMessagesForChatRoom(chatRoomId);
    }

    public boolean isValidChat(String username, String recieverUsername) {
        return chatRoomDB.isValidChat(username, recieverUsername);
    }
}
