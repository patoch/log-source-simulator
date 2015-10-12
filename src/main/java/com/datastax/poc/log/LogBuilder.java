package com.datastax.poc.log;

import com.datastax.poc.log.utils.Random;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Patrick on 17/09/15.
 */
public abstract class LogBuilder
{

    public static Log buildLog(UUID sourceId, Date ts, String type, String raw, int bucketTimeInSeconds)
    {
        Log log = new Log();
        log.setSourceId(sourceId);
        log.setType(type);
        log.setTimestamp(ts);
        log.setBucketTs(bucketTimeInSeconds);
        log.setRaw(raw);
        return log;
    }


}
