package com.datastax.poc.log.utils;

/**
 * Created by Patrick on 12/10/15.
 */
public abstract class Env {

    public static String getString(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            value = defaultValue;
        }
        System.out.println(String.format("%s: %s", name, value));
        return value;
    }

    public static int getInt(String name, int defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            value = String.valueOf(defaultValue);
        }
        System.out.println(String.format("%s: %s", name, value));
        return Integer.parseInt(value);
    }
}
