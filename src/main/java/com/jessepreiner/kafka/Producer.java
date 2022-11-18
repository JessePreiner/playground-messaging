package com.jessepreiner.kafka;

import com.google.gson.Gson;
import com.jessepreiner.Id;
import com.jessepreiner.Message;
import com.thedeanda.lorem.LoremIpsum;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Producer {

    public static final String CHATS_TOPIC = "chats";
    public static final String[] AUTHORS = new String[]{"Jesse Preiner", "Jennifer Ballantyne", "Jordan Mears", "Ashleigh Mattern"};
    public static final String[] CHAT_ROOMS = new String[]{"cats", "dogs", "tech", "futsal"};
    private static final short NUM_PARTITIONS = 10;

    public Producer() {
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        String password = System.getenv("PASSWORD");
        String username = System.getenv("USERNAME");
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");

        var props = new Properties();
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + username + "\" password=\"" + password + "\";");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("bootstrap.servers", bootstrapServers);
        props.put("security.protocol", "SASL_SSL");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        System.out.println(props);
        try (var producer = new KafkaProducer<String, String>(props)) {
            for (int i = 0; i < 20; i++) {
                int randomIdx = ThreadLocalRandom.current().nextInt(0, AUTHORS.length);
                int randomIdx2 = ThreadLocalRandom.current().nextInt(0, CHAT_ROOMS.length);
                String author = AUTHORS[randomIdx];
                String chatRoomName = CHAT_ROOMS[randomIdx2];
                String chatsTopic = MessageFormat.format("{0}.{1}", CHATS_TOPIC, chatRoomName);
                Message message = new Message(UUID.randomUUID(), chatsTopic, author, LoremIpsum.getInstance().getWords(1, 156));
                String serKey = gson.toJson(new Id(message.getMessageId()));
                String serMsg = gson.toJson(message);

                ProducerRecord<String, String> record = new ProducerRecord<>(chatsTopic, getPartition(message), serKey, serMsg);
                System.out.println(record);

                produceRecord(producer, record);
            }
        }
    }

    private static void produceRecord(KafkaProducer<String, String> producer, ProducerRecord<String, String> record) {
        producer.send(
                record,
                (a,b) -> {
                    if (b != null) {
                        System.out.printf("Exception occurred: " + b.getMessage());
                        b.printStackTrace();
                    } else {
                        String stringBuilder = "\n\ttopic: " + a.topic() +
                                "\n\toffset: " + a.offset() +
                                "\n\tpartition: " + a.partition() +
                                "\n\ttimestamp: " + a.timestamp();
                        System.out.println("Result: " + stringBuilder);
                    }
                });
    }

    private static int getPartition(Message message) {
        return (message.getChannelName().hashCode() & Integer.MAX_VALUE) % NUM_PARTITIONS;
    }
}
