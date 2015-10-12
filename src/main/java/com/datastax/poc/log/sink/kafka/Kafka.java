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
public abstract class Kafka {

    private static Producer<String, String> producer;

    public static synchronized Producer<String, String> getProducer() {
        if (producer == null) {
            Properties props = new Properties();

            props.put("metadata.broker.list", "127.0.0.1:9092");
            props.put("serializer.class", "kafka.serializer.StringEncoder");
            //props.put("partitioner.class", "example.producer.SimplePartitioner");
            props.put("request.required.acks", "1");

            ProducerConfig config = new ProducerConfig(props);
            producer = new Producer<String, String>(config);
        }
        return producer;
    }

}
