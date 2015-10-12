package com.datastax.poc.log.sink.kafka;

import com.datastax.poc.log.utils.Env;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by Patrick on 12/10/15.
 */
public abstract class Kafka {

    private final static String BROKER_LIST = "kafka_broker_list";

    private static Producer<String, String> producer;

    public static synchronized Producer<String, String> getProducer() {
        if (producer == null) {
            Properties props = new Properties();

            props.put("metadata.broker.list", Env.getString(BROKER_LIST, "127.0.0.1:9092"));
            props.put("serializer.class", "kafka.serializer.StringEncoder");
            //props.put("partitioner.class", "example.producer.SimplePartitioner");

            props.put("request.required.acks", "1");

            ProducerConfig config = new ProducerConfig(props);
            producer = new Producer<String, String>(config);
        }
        return producer;
    }

}
