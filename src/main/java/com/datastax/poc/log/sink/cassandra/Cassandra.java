package com.datastax.poc.log.sink.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.*;
import com.datastax.poc.log.utils.Env;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by patrick on 03/02/15.
 */
public abstract class Cassandra {

    private final static String CONTACT_POINTS = "cassandra_contact_points";
    private final static String LOCAL_DC = "cassandra_local_dc";
    private final static String READ_TIMEOUT = "cassandra_read_timeout";

    private static Cluster s_cluster;
    private static Session s_session;
    private static Map<String, PreparedStatement> s_psMap;


    public static synchronized Cluster getCluster() {

        if (s_cluster == null) {

            QueryOptions queryOptions = new QueryOptions()
                    .setConsistencyLevel(ConsistencyLevel.QUORUM)
                    .setFetchSize(100);

            SocketOptions socketOptions = new SocketOptions()
                    .setReadTimeoutMillis(Env.getInt(READ_TIMEOUT, 10500));

            s_cluster = Cluster.builder()
                    .withQueryOptions(queryOptions)
                    .withSocketOptions(socketOptions)
                    .addContactPoints(Env.getString(CONTACT_POINTS, "127.0.0.1"))
                    .withProtocolVersion(ProtocolVersion.V3)
                    .withPort(9042)
                    //.withCompression(ProtocolOptions.Compression.LZ4)
                    .withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy(Env.getString(LOCAL_DC, "Cassandra")), true))
                    .withRetryPolicy(new LoggingRetryPolicy(DefaultRetryPolicy.INSTANCE))
                    .withReconnectionPolicy( new ExponentialReconnectionPolicy(100, 30000))
                    .withoutJMXReporting()
                    .withoutMetrics()
                    .build();

        }
        return s_cluster;
    }

    public static synchronized Session getSession() {
        if (s_session == null) {
            s_session = getCluster().connect();
        }
        return s_session;
    }

    public static void prepareStatement(String key, String cql) {
        if (s_psMap == null) {
            s_psMap = new HashMap<String, PreparedStatement>();
        }
        s_psMap.put(key, getSession().prepare(cql));
    }

    public static PreparedStatement getPreparedStatement(String key) {
        return s_psMap.get(key);
    }

    public static synchronized void shutdown() {
        s_session.close();
        s_cluster.close();
    }

}
