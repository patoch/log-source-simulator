package com.datastax.poc.log;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.poc.log.sink.LogSinkFactory;
import com.datastax.poc.log.sink.LogSink;
import com.datastax.poc.log.utils.Random;
import org.apache.commons.cli.*;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.datastax.poc.log.sink.LogSinkFactory.SinkType;

/**
 * Created by Patrick on 12/10/15.
 */
public class LogSourceSimulator {

    public static void main(String[] args) {

        // Simulator settings
        String sink = null;
        UUID sourceId = null;
        String[] logTypes = null;
        int bucketTimeInSeconds = -1;
        int threadCount = -1;
        int pauseTime = -1;
        int logsTosend = -1;

        // Parse command line
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption( "k", "sink", true, "Target sink [kafka|cassandra]. Kafka by default.");
        options.addOption( "s", "source", true, "Log source id. Random by default.");
        options.addOption( "l", "log-types", true, "Comma separated list of log types.");
        options.addOption( "b", "bucket-time", true, "Bucket time in seconds. 300 s by default.");
        options.addOption( "t", "thread-count", true, "Number of threads. 5 by default.");
        options.addOption( "p", "pause", true, "Pause in ms between each created log. 5ms by default.");
        options.addOption( "n", "numlogs", true, "Number of logs to send.");


        try {
            CommandLine line = parser.parse(options, args);
            sink = line.getOptionValue("sink", "kafka").toUpperCase();
            sourceId = UUID.fromString(line.getOptionValue("source", UUIDs.timeBased().toString()));
            logTypes = line.getOptionValue("log-types", "view_category,view_product,search,buy_product,like_product").split(",");
            bucketTimeInSeconds = Integer.parseInt(line.getOptionValue("bucket-time", "300"));
            threadCount = Integer.parseInt(line.getOptionValue("thread-count", "5"));
            pauseTime = Integer.parseInt(line.getOptionValue("pause", "5"));
            logsTosend = Integer.parseInt(line.getOptionValue("numlogs", "100"));
        }
        catch( ParseException e ) {
            System.out.println( "Unexpected exception:" + e.getMessage() );
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Log source simulator", options, true);
            System.exit(1);
        }


        // Create simulator
        final LogSourceSimulator sim = new LogSourceSimulator(SinkType.valueOf(sink), sourceId, logTypes, bucketTimeInSeconds, threadCount, pauseTime, logsTosend);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                sim.stop();
            }
        });
        sim.start();
    }


    private final UUID sourceId;
    private final String[] logsTypes;
    private final int threadCount;
    private final int sleepMs;
    private int logCount;
    private int bucketTimeInSeconds;
    private LogSinkFactory.SinkType sinkType;
    private LogSink sink;
    private ExecutorService executorService;
    private int logsToSend;


    public LogSourceSimulator(SinkType sinkType, UUID sourceId, String[] logTypes, int bucketTimeInSeconds, int threadCount, int sleepMs, int logsToSend) {
        this.sinkType = sinkType;
        this.sourceId = sourceId;
        this.logsTypes = logTypes;
        this.bucketTimeInSeconds = bucketTimeInSeconds;
        this.threadCount = threadCount;
        this.sleepMs = sleepMs;
        this.logCount = 0;
        this.logsToSend = logsToSend;
    }


    public void start() {
        executorService = Executors.newFixedThreadPool(threadCount);
        sink = LogSinkFactory.getInstance(sinkType);
        System.out.println("[Log simulator started]");
        run();
    }

    private void run() {

        while (true) {

            if (executorService.isShutdown() || logCount >= logsToSend) {
                break;
            }

            createLog();
            logCount ++;

            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                stop();
            }
        }
    }

    public void stop() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            System.out.println("Waiting for termination");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sink.shutdown();
        System.out.println("[Log simulator stopped]");
        System.out.println(String.format("%d logs sent", logCount));
    }

    private void createLog() {
        executorService.execute(new Runnable() {
            public void run() {
                Log log = LogBuilder.buildLog(sourceId, new Date(), Random.getFromArray(logsTypes), Random.getAsciiString(1000), bucketTimeInSeconds);
                sink.push(log);
                System.out.print(".");
            }
        });
    }

}
