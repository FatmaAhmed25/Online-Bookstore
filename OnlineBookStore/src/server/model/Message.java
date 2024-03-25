package server.model;

import java.time.LocalDateTime;

public class Message {
    private String senderUsername;
    private String recipientUsername;
    private String content;
    private LocalDateTime timestamp;

    public Message(String senderUsername, String recipientUsername, String content) {
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.content = content;
        this.timestamp = LocalDateTime.now(); // Timestamp the message with the current time
    }

    // Getters and setters
    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Override toString() method for debugging or displaying messages
    @Override
    public String toString() {
        return "[" + timestamp.toString() + "] " + senderUsername + " -> " + recipientUsername + ": " + content;
    }
}