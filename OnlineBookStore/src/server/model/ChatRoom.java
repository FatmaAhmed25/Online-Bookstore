package server.model;

import java.util.ArrayList;

public class ChatRoom {
    int chatRoomId;
    int requesterId;
    int lenderId;
    String requesterUsername;
    String lenderUsername;
    ArrayList<Message>messages;
    public ChatRoom(int id, int requesterId,int lenderId) {
        this.chatRoomId=id;
        this.requesterId=requesterId;
        this.lenderId = lenderId;
    }

    public ChatRoom(int id, int requesterId, int lenderId, String requesterUsername, String lenderUsername) {
        this.chatRoomId=id;
        this.requesterId=requesterId;
        this.lenderId = lenderId;
        this.requesterUsername=requesterUsername;
        this.lenderUsername=lenderUsername;

    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }
    public ArrayList<Message> getMessages() {
        return messages;
    }
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
    public void addToMessages(Message message)
    {
        this.messages.add(message);
    }

    public String getLenderUsername() {
        return lenderUsername;
    }

    public void setLenderUsername(String lenderUsername) {
        this.lenderUsername = lenderUsername;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatRoomId=" + chatRoomId +
                ", requesterUsername='" + requesterUsername + '\'' +
                ", lenderUsername='" + lenderUsername + '\'' +
//                ", Last Messages=" + messages.get(messages.size()-1) +
                '}';
    }
}
