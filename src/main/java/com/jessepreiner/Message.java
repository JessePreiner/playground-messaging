package com.jessepreiner;

import java.util.Objects;
import java.util.UUID;

public class Message {
    final UUID messageId;
    final String channelName;
    final String author;
    final String body;

    public Message(UUID messageId, String channelName, String author, String body) {
        this.messageId = messageId;
        this.channelName = channelName;
        this.author = author;
        this.body = body;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", channelName='" + channelName + '\'' +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId) && Objects.equals(channelName, message.channelName) && Objects.equals(author, message.author) && Objects.equals(body, message.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, channelName, author, body);
    }
}
