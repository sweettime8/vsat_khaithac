package com.elcom.rule.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author hanh props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
 * VsatPartitioner.class.getName());
 * org.apache.kafka.clients.producer.RoundRobinPartitioner
 */
public class VsatPartitioner implements Partitioner {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        
        int partitionNumberValue = 0;
        if( partitions.size() > 1 )
            partitionNumberValue = (new Random()).nextInt(partitions.size() - 1);
        
//        System.out.println("partitionNumberValue: " + partitionNumberValue);
        
        return partitionNumberValue;
    }
    
    @Override
    public void close() {
    }
}
