package com.rewetest.emailgeneratorservice.kafka;

import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailProducerTest {

    @Mock
    private KafkaTemplate<String, EmailDTO> kafkaTemplate;

    @Mock
    private NewTopic topic;

    @InjectMocks
    private EmailProducer emailProducer;

    @Captor
    private ArgumentCaptor<Message<EmailDTO>> messageCaptor;

    @BeforeEach
    public void setUp() {
        when(topic.name()).thenReturn("testTopic");
    }

    @Test
    public void whenProduceEmail_thenSendToKafka() throws ExecutionException, InterruptedException {
        // Arrange
        EmailDTO emailDTO = new EmailDTO("Test", "This is a test email.", "sender@example.com", Collections.singletonList("recipient@example.com"));
        CompletableFuture<SendResult<String, EmailDTO>> future = new CompletableFuture<>();

        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("testTopic", 1), 0L, 0L, 0L, 0L, 0, 0);
        SendResult<String, EmailDTO> sendResult = new SendResult<>(null, recordMetadata);
        future.complete(sendResult);

        when(kafkaTemplate.send(any(Message.class))).thenReturn(future);

        // Act
        emailProducer.produce(emailDTO);

        // Assert
        verify(kafkaTemplate, times(1)).send(messageCaptor.capture());
        Message<EmailDTO> messageSent = messageCaptor.getValue();

        assertEquals(emailDTO, messageSent.getPayload());
        assertEquals("testTopic", messageSent.getHeaders().get(KafkaHeaders.TOPIC));
        assertEquals(emailDTO.getSender(), messageSent.getHeaders().get(KafkaHeaders.KEY));
    }
}
