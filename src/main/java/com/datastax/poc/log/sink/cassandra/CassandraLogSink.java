package com.datastax.poc.log.sink.cassandra;

import com.datastax.poc.log.Log;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.poc.log.sink.LogSink;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Patrick on 17/09/15.
 */
public class CassandraLogSink implements LogSink {

    private static CassandraLogSink s_onlyInstance;
    private Mapper<Log> mapper;

    public static synchronized CassandraLogSink getInstance() {
        if (s_onlyInstance == null) {
            s_onlyInstance = new CassandraLogSink();
        }
        return s_onlyInstance;
    }

    private CassandraLogSink() {
        mapper = new MappingManager(Cassandra.getSession()).mapper(Log.class);
    }

    public void push(Log log) {
        mapper.save(log);
    }

    public Log find(UUID sourceId, Date bucketTs, Date timestamp, UUID id) {
        return mapper.get(sourceId, bucketTs, timestamp, id);
    }

    public void shutdown() {
        Cassandra.shutdown();
    }


}
