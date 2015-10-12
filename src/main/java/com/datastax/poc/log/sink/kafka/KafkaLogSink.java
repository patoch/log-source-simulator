package com.datastax.poc.log.sink.kafka;

import com.datastax.poc.log.Log;
import com.datastax.poc.log.sink.LogSink;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Patrick on 12/10/15.
 */
public class KafkaLogSink implements LogSink {

    private static KafkaLogSink onlyInstance;

    public static synchronized LogSink getInstance() {
        if (onlyInstance == null) {
            onlyInstance = new KafkaLogSink();
        }
        return onlyInstance;
    }


    private Producer<String, String> producer;


    private KafkaLogSink() {
        producer = Kafka.getProducer();
    }


    public void push(Log log) {
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("logs", log.toString());
        producer.send(data);
    }

    public Log find(UUID sourceId, Date bucketTs, Date timestamp, UUID id) {
        throw new RuntimeException("Find method, not implemented for Kafka sink.");
    }

    public void shutdown() {
        producer.close();
    }

}
