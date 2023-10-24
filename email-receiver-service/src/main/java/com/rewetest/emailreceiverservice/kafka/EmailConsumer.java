package com.rewetest.emailreceiverservice.kafka;

import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import com.rewetest.sharedlibrary.emailstatistics.dto.EmailStatisticsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailConsumer {
    private final WebClient.Builder webClientBuilder;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "${spring.kafka.topic.name}", partitions = {"#{emailPartitions}"})},
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload EmailDTO emailDTO,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Received {} message from Partition {}: {}", key, partition, emailDTO);
        forwardEmailStatistics(emailDTO);
    }

    private WebClient getWebClient() {
        return webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    public void forwardEmailStatistics(EmailDTO emailDTO) {
        WebClient webClient = getWebClient();
        EmailStatisticsDTO emailStatisticsDTO = mapToEmailStatisticsDTO(emailDTO);

        Mono<HttpStatus> response = webClient.post()
                .uri("/api/v1/statistics/create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(emailStatisticsDTO)
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        clientResponse -> Mono.error(new RuntimeException("Error while updating statistics: " + clientResponse.statusCode())))
                .bodyToMono(Void.class)
                .thenReturn(HttpStatus.CREATED);

        response.subscribe(
                result -> {
                    if (result == HttpStatus.CREATED) {
                        log.info("Statistics updated successfully");
                    }
                },
                error -> log.error("An error occurred during statistics creation: {}", error.getMessage())
        );                 //TODO ADD ERROR HANDLING HERE AND IMPLEMENT GLOBAL EXCEPTION HANDLER

    }

    private EmailStatisticsDTO mapToEmailStatisticsDTO(EmailDTO emailDTO) {
        EmailStatisticsDTO emailStatisticsDTO = new EmailStatisticsDTO();

        emailStatisticsDTO.setSender(emailDTO.getSender());
        emailStatisticsDTO.setSubject(emailDTO.getTopic());
        emailStatisticsDTO.setContent(emailDTO.getContent());
        emailStatisticsDTO.setRecipients(emailDTO.getRecipients());

        return emailStatisticsDTO;
    }
}
