package com.datastax.poc.log.sink;

import com.datastax.poc.log.sink.cassandra.CassandraLogSink;

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
        }
        throw new RuntimeException("Sink " + type + " not implemented.");
    }
}
