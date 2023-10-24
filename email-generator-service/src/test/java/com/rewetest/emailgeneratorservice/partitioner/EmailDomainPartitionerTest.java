package com.rewetest.emailgeneratorservice.partitioner;

import org.apache.kafka.common.Cluster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EmailDomainPartitionerTest {

    private EmailDomainPartitioner partitioner;
    private Cluster cluster;

    @BeforeEach
    void setUp() {
        partitioner = new EmailDomainPartitioner();
        cluster = mock(Cluster.class);
    }

    @Test
    void givenKnownEmailDomain_whenPartition_thenReturnsExpectedPartition() {
        Map<String, Integer> knownDomains = new HashMap<>();
        knownDomains.put("gmail.com", 0);
        knownDomains.put("yahoo.com", 1);

        for (Map.Entry<String, Integer> entry : knownDomains.entrySet()) {
            String key = "user@" + entry.getKey();
            int partition = partitioner.partition("test-topic", key, null, null, null, cluster);

            assertEquals(entry.getValue(), partition, "Partition for " + entry.getKey() + " should be " + entry.getValue());
        }
    }

    @Test
    void givenUnknownEmailDomain_whenPartition_thenThrowsException() {
        String unknownDomain = "unknown.com";
        String key = "user@" + unknownDomain;

        Exception exception = assertThrows(RuntimeException.class, () ->
                partitioner.partition("test-topic", key, null, null, null, cluster));

        assertTrue(exception.getMessage().contains("Encountered an unknown email domain"));
    }
}