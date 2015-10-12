package com.datastax.poc.log.sink;

import com.datastax.poc.log.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Patrick on 02/10/15.
 */
public interface LogSink {

    void push(Log log);

    Log find(UUID sourceId, Date bucketTs, Date timestamp, UUID id);

    void shutdown();
}
