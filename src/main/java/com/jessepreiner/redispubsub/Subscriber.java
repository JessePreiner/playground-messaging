package com.jessepreiner.redispubsub;

import redis.clients.jedis.Jedis;

import static com.jessepreiner.redispubsub.Publisher.CHANNEL;

public class Subscriber
{
    public static void main(String[] args ) {
        Jedis jedis = new Jedis();
        jedis.subscribe(new ChatPubSub(), CHANNEL);
    }

}
