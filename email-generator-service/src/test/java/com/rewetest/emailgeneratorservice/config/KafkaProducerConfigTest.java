package com.rewetest.emailgeneratorservice.config;

import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = KafkaProducerConfig.class)
public class KafkaProducerConfigTest {

    @Autowired
    private DefaultKafkaProducerFactory<String, EmailDTO> producerFactory;

    @Autowired
    private KafkaTemplate<String, EmailDTO> kafkaTemplate;

    @Autowired
    private NewTopic topic;

    @Test
    public void testProducerFactoryConfigurations() {

        var configs = producerFactory.getConfigurationProperties();

        assertEquals("localhost:9092", configs.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("org.apache.kafka.common.serialization.StringSerializer", configs.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        assertEquals("org.springframework.kafka.support.serializer.JsonSerializer", configs.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
        assertEquals("com.rewetest.emailgeneratorservice.partitioner.EmailDomainPartitioner", configs.get(ProducerConfig.PARTITIONER_CLASS_CONFIG));
    }

    @Test
    public void testKafkaTemplate() {
        assertNotNull(kafkaTemplate, "Kafka template should not be null");
    }

    @Test
    public void testTopicConfiguration() {
        assertNotNull(topic, "Topic should not be null");
        assertEquals("email_topic", topic.name());
    }
}