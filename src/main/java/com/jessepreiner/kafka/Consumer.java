package com.jessepreiner.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class Consumer {
    public static void main(String[] args) throws Exception {
        var props = new Properties();
        String password = System.getenv("PASSWORD");
        String username = System.getenv("USERNAME");
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");

        props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + username + "\" password=\"" + password + "\";");
        props.put("bootstrap.servers", bootstrapServers);
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("security.protocol", "SASL_SSL");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        props.put("group.id", "$GROUP_NAME");

        try(var consumer = new KafkaConsumer<String, String>(props)) {

            // ...
        }

    }
}
