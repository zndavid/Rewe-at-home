package com.rewetest.emailgeneratorservice.config;

import com.rewetest.emailgeneratorservice.partitioner.EmailDomainPartitioner;
import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.rewetest.sharedlibrary.email.utility.EmailUtility.DOMAIN_PARTITION_MAP;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Bean
    public DefaultKafkaProducerFactory<String, EmailDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, EmailDomainPartitioner.class.getName());

        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topicName)
                .partitions(DOMAIN_PARTITION_MAP.size())
                .build();
    }

    @Bean
    public KafkaTemplate<String, EmailDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
