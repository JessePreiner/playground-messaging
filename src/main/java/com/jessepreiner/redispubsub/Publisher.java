package com.jessepreiner.redispubsub;

import com.google.gson.Gson;
import com.jessepreiner.Message;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Hello world!
 */
@SuppressWarnings("SameParameterValue")
public class Publisher {

    public static final String CHANNEL = "chat/messages/add";
    private static final Gson gson = new Gson();

    public static void main(String[] args) {

        try (Jedis jedis = new Jedis()) {
            String message = getMessage(UUID.randomUUID(), "jessepreiner", "bodybody?");
            jedis.publish(CHANNEL, message);
        }

    }

    private static String getMessage(UUID messageId, String author, String body) {
        Message message = new Message(messageId, CHANNEL, author, body);
        return gson.toJson(message);
    }

}
