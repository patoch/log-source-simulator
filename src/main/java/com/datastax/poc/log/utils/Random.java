package com.datastax.poc.log.utils;

import org.apache.commons.lang.RandomStringUtils;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Patrick on 12/10/15.
 */
public class Random {

    public static String getAsciiString(int count) {
        return RandomStringUtils.randomAscii(count);
    }

    public static String getFromArray(String[] array) {
        return array[ThreadLocalRandom.current().nextInt(0, array.length)];
    }

    public static Date getDateBetween(Date fromTS, Date toTS) {
        return new Date(ThreadLocalRandom.current().nextLong(fromTS.getTime(), toTS.getTime()));
    }

}
