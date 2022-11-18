package com.jessepreiner.redispubsub;

import com.google.gson.Gson;
import com.jessepreiner.Message;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ChatPubSub extends JedisPubSub {
    private final Map<UUID, Message> messages;

    public ChatPubSub() {
        this.messages = new HashMap<>();
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> System.out.printf("Messages are currently %s\n", messages),
                5, 5, TimeUnit.SECONDS);

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.printf("subscribing to %s (%d total)\n", channel, subscribedChannels);
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void subscribe(String... channels) {
        System.out.println("channels: " + Arrays.toString(channels));
        super.subscribe(channels);
    }

    @Override
    public void onMessage(String channel, String message) {
        saveMessage(new Gson().fromJson(message, Message.class));
        System.out.println("onMessage: " + message + " to channel " + channel);
        super.onMessage(channel, message);
    }

    private void saveMessage(Message message) {
        this.messages.put(message.getMessageId(), message);
    }
}
