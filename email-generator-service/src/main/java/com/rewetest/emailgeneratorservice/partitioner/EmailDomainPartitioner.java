package com.rewetest.emailgeneratorservice.partitioner;

import com.rewetest.sharedlibrary.email.utility.EmailUtility;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.Optional;

public class EmailDomainPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String emailDomain = extractDomainFromEmailKey(key);
        return Optional.ofNullable(EmailUtility.DOMAIN_PARTITION_MAP.get(emailDomain)).orElseThrow(() ->
                new RuntimeException("Encountered an unknown email domain: " + emailDomain));
    }

    private String extractDomainFromEmailKey(Object key) {
        String email = (String) key;
        return email.split("@")[1];
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}

