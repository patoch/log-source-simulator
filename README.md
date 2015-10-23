# Log source simulator

This simulator produces random logs real time. These can be pushed to Cassandra or Kafka for testing purpose.


**Build**

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

**Using with cassandra**

Create keyspace and table :
```
DROP KEYSPACE IF EXISTS log_ks;
CREATE KEYSPACE log_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

USE log_ks;

CREATE TABLE IF NOT EXISTS logs (
 source_id TIMEUUID,
 bucket_ts TIMESTAMP,
 ts TIMESTAMP,
 id UUID,
 type TEXT,
 tags MAP<TEXT,TEXT>,
 timestamps MAP<TIMESTAMP, TEXT>,
 raw TEXT,
 PRIMARY KEY (( source_id, bucket_ts ),  ts, id)
)
WITH default_time_to_live = 26780400;
```
 
Export Cassandra settings if not running locally:
```
export cassandra_contact_points="..."
export cassandra_local_dc="..."
export cassandra_read_timeout="..."
```

**Using with Kafka**

Create the topic "logs"

Export Kafka settings if not running locally:
```
export kafka_broker_list="ip1:9092,ip2:9092"
```
 
 
 