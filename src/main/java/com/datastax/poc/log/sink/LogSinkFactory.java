package com.datastax.poc.log.sink;

import com.datastax.poc.log.sink.cassandra.CassandraLogSink;
import com.datastax.poc.log.sink.kafka.KafkaLogSink;

/**
 * Created by Patrick on 02/10/15.
 */
public class LogSinkFactory {

    public enum SinkType {
        CASSANDRA,
        KAFKA
    }

    public static LogSink getInstance(SinkType type) {
        if (type == SinkType.CASSANDRA) {
            return CassandraLogSink.getInstance();
        } else if (type == SinkType.KAFKA) {
            return KafkaLogSink.getInstance();
        }
        throw new RuntimeException("Sink " + type + " not implemented.");
    }
}
