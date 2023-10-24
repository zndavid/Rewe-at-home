package com.rewetest.emailreceiverservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@DirtiesContext
@EmbeddedKafka(partitions = 3,  brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmailConsumerTest {

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;
    private Producer<String, Object> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private EmailConsumer emailConsumer;

    @Captor
    ArgumentCaptor<EmailDTO> emailDTOArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> partitionArgumentCaptor;

    @Captor
    ArgumentCaptor<String> keyArgumentCaptor;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));

        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>()).createProducer();
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }

    @Test
    void consume() throws JsonProcessingException {
        EmailDTO emailDTO = new EmailDTO("Test Subject", "This is the email content.", "john@gmail.com", Collections.singletonList("recipient@example.com"));
        String key = "gmail.com";
        producer.send(new ProducerRecord<>(TOPIC_NAME,1, key, emailDTO));
        producer.flush();
        doNothing().when(emailConsumer).forwardEmailStatistics(any(EmailDTO.class));

        // Read the message and assert its properties
        verify(emailConsumer, timeout(5000).times(1))
                .consume(emailDTOArgumentCaptor.capture(),
                        partitionArgumentCaptor.capture(),
                        keyArgumentCaptor.capture());

        EmailDTO receivedEmail = emailDTOArgumentCaptor.getValue();
        assertNotNull(receivedEmail);
        assertEquals("Test Subject", receivedEmail.getTopic());
        assertEquals("This is the email content.", receivedEmail.getContent());
        assertEquals("john@gmail.com", receivedEmail.getSender());
        assertEquals(Collections.singletonList("recipient@example.com"), receivedEmail.getRecipients());
        assertEquals(1, partitionArgumentCaptor.getValue());
        assertEquals("gmail.com", keyArgumentCaptor.getValue());
    }

}
