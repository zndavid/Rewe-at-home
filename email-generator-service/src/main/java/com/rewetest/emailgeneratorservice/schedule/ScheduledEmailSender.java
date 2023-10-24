package com.rewetest.emailgeneratorservice.schedule;

import com.rewetest.emailgeneratorservice.kafka.EmailProducer;
import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import com.rewetest.sharedlibrary.email.utility.EmailUtility;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduledEmailSender {

    private final EmailProducer emailProducer;

    private final ThreadPoolTaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledTask;

    private volatile boolean isScheduleEnabled = false;

    @PostConstruct
    public void initialize() {
        taskScheduler.initialize();
    }

    @PreDestroy
    public void shutDown() {
        taskScheduler.shutdown();
    }

    public void startSchedule() {
        if (isScheduleEnabled) {
            return;
        }
        isScheduleEnabled = true;
        log.info("Scheduled Task has been started.");
        scheduledTask = taskScheduler.scheduleAtFixedRate(this::executeTask, Duration.ofSeconds(2));
    }

    public void stopSchedule() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTask = null;
        }
        isScheduleEnabled = false;
        log.info("Scheduled Task has been stopped.");
    }

    public void executeTask() {
        try {
            log.info("Scheduled Task is starting.");
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setContent(EmailUtility.getRandomContent());
            emailDTO.setSender(EmailUtility.generateRandomEmail());
            emailDTO.setTopic(EmailUtility.getRandomSubject());
            emailDTO.setRecipients(EmailUtility.generateRandomRecipients(10));
            emailProducer.produce(emailDTO);
            log.info("Scheduled Task has completed.");
        } catch (Exception e) {
            log.error("An error occurred during the execution of the scheduled task", e);
        }
    }
}