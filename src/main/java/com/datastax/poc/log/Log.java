package com.datastax.poc.log;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Patrick on 17/09/15.
 */

@Table(keyspace = "log_ks", name = "logs")
public class Log {

    @PartitionKey(0)
    @Column(name = "source_id")
    private UUID sourceId;

    @PartitionKey(1)
    @Column(name = "bucket_ts")
    private Date bucketTs;

    @ClusteringColumn(0)
    @Column(name = "ts")
    private Date timestamp;

    @ClusteringColumn(1)
    @Column(name = "id")
    private UUID id;

    @Column(name = "type")
    private String type;

    @Column(name = "tags")
    private Map<String, String> tags;

    @Column(name = "timestamps")
    private Map<Date, String> timestamps;

    @Column(name = "raw")
    private String raw;


    public Log(UUID id) {
        this.id = id;
    }

    public Log() {
        this(UUIDs.random());
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public Date getBucketTs() {
        return bucketTs;
    }

    public void setBucketTs(int intervalInSeconds) {
        long bucketTime = this.timestamp.getTime() - this.timestamp.getTime() % (intervalInSeconds * 1000);
        this.bucketTs = new Date(bucketTime);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBucketTs(Date bucketTs) {
        this.bucketTs = bucketTs;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void addTag(String name, String value) {
        this.tags.put(name, value);
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void addTimestamp(Date ts, String name) {
        this.timestamps.put(ts, name);
    }

    public Map<Date, String> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Map<Date, String> timestamps) {
        this.timestamps = timestamps;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw.replaceAll(";","");
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }


    @Override
    public String toString() {
        return String.format("%s;%s;%s", sourceId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(this.timestamp), raw);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Log log = (Log) o;

        if (sourceId != null ? !sourceId.equals(log.sourceId) : log.sourceId != null) return false;
        if (bucketTs != null ? !bucketTs.equals(log.bucketTs) : log.bucketTs != null) return false;
        if (timestamp != null ? !timestamp.equals(log.timestamp) : log.timestamp != null) return false;
        if (id != null ? !id.equals(log.id) : log.id != null) return false;
        if (type != null ? !type.equals(log.type) : log.type != null) return false;
        if (tags != null ? !tags.equals(log.tags) : log.tags != null) return false;
        if (timestamps != null ? !timestamps.equals(log.timestamps) : log.timestamps != null) return false;
        return !(raw != null ? !raw.equals(log.raw) : log.raw != null);

    }

    @Override
    public int hashCode() {
        int result = sourceId != null ? sourceId.hashCode() : 0;
        result = 31 * result + (bucketTs != null ? bucketTs.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (timestamps != null ? timestamps.hashCode() : 0);
        result = 31 * result + (raw != null ? raw.hashCode() : 0);
        return result;
    }
}
