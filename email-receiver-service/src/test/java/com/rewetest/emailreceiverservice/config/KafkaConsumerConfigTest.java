package com.rewetest.emailreceiverservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConsumerConfigTest {

    @InjectMocks
    private KafkaConsumerConfig kafkaConsumerConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(kafkaConsumerConfig, "bootstrapServers", "testBootstrapServers");
        ReflectionTestUtils.setField(kafkaConsumerConfig, "groupId", "testGroup");
        ReflectionTestUtils.setField(kafkaConsumerConfig, "offsetReset", "earliest");
        ReflectionTestUtils.setField(kafkaConsumerConfig, "keyDeserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        ReflectionTestUtils.setField(kafkaConsumerConfig, "valueDeserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    @Test
    void testConsumerFactory() {
        ConsumerFactory<String, String> consumerFactory = kafkaConsumerConfig.consumerFactory();

        assertNotNull(consumerFactory, "ConsumerFactory should not be null");
    }

    @Test
    void testEmailPartitions() {
        String emailPartitions = kafkaConsumerConfig.emailPartitions();

        assertNotNull(emailPartitions, "Email partitions should not be null");
    }

    @Test
    void testKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaConsumerConfig.kafkaListenerContainerFactory();

        assertNotNull(factory, "KafkaListenerContainerFactory should not be null");
    }
}