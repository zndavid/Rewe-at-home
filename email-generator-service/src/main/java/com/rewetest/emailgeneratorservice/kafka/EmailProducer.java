package com.rewetest.emailgeneratorservice.kafka;

import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailProducer {
    private final NewTopic topic;
    private final KafkaTemplate<String, EmailDTO> kafkaTemplate;

    public void produce(EmailDTO emailDTO) {
        Message<EmailDTO> message = MessageBuilder
                .withPayload(emailDTO)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .setHeader(KafkaHeaders.KEY, emailDTO.getSender())
                .build();
        CompletableFuture<SendResult<String, EmailDTO>> completableFuture = kafkaTemplate.send(message);
        completableFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("Failed to produce email and send it to Kafka", throwable);
            } else {
                log.info("Sent email [{}] with offset [{}]", emailDTO, result.getRecordMetadata().offset());
            }
        });
    }
}
