# Log source simulator

This simulator produces random logs real time. These can be pushed to Cassandra or Kafka for testing purpose.


**Build the simulator**

At the project root:
```
mvn package
```

**Run** 

To run it locally with Cassandra or Kafka, execute the jar: 

```
java -jar log-source-simulator-1.0-SNAPSHOT-jar-with-dependencies.jar


Options:
usage: Log source simulator [-b <arg>] [-k <arg>] [-l <arg>] [-p <arg>]
       [-s <arg>] [-t <arg>]
 -b,--bucket-time <arg>    Bucket time in seconds. 300 s by default.
 -k,--sink <arg>           Target sink [kafka|cassandra]. Kafka by
                           default.
 -l,--log-types <arg>      Comma separated list of log types.
 -p,--pause <arg>          Pause in ms between each created log. 5ms by
                           default.
 -s,--source <arg>         Log source id. Random by default.
 -t,--thread-count <arg>   Number of threads. 5 by default.
 ```
 
To change the cassandra or kafka connexion information, set environment variables :

Cassandra:
```
export cassandra_contact_points="..."
export cassandra_local_dc="..."
export cassandra_read_timeout="..."
```

Kafka
```
export kafka_broker_list="..."
```
 
 
 