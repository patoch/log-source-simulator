package com.datastax.poc.log;


import com.datastax.driver.core.utils.UUIDs;
import com.datastax.poc.log.sink.LogSink;
import com.datastax.poc.log.sink.LogSinkFactory;
import com.datastax.poc.log.utils.Random;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by Patrick on 25/09/15.
 */
public class LogSinkTest {

    final static String[] TYPES = {"search_product", "view_product", "select_category", "view_category_products"};
    static int s_bucketTimeInSeconds = 300;
    static LogSink s_sink = LogSinkFactory.getInstance(LogSinkFactory.SinkType.CASSANDRA);
    static UUID s_sourceId = UUIDs.timeBased();

    @Test
    public void insertAndFindLog()
    {
        Log log = LogBuilder.buildLog(s_sourceId, new Date(), Random.getFromArray(TYPES), Random.getAsciiString(500), s_bucketTimeInSeconds);
        s_sink.push(log);

        Log log2 = s_sink.find(log.getSourceId(), log.getBucketTs(), log.getTimestamp(), log.getId());

        assertEquals(log, log2);
    }

}
