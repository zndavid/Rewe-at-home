package com.rewetest.emailgeneratorservice.schedule;

import com.rewetest.emailgeneratorservice.kafka.EmailProducer;
import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduledEmailSenderTest {

    @Mock
    private EmailProducer emailProducer;

    @Mock
    private ThreadPoolTaskScheduler taskScheduler;

    @InjectMocks
    private ScheduledEmailSender scheduledEmailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenStartSchedule_thenVerifyTaskScheduled() {

        scheduledEmailSender.startSchedule();

        verify(taskScheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(2)));
    }

    @Test
    void testStopSchedule_setsScheduledTaskToNull() throws NoSuchFieldException, IllegalAccessException {
        scheduledEmailSender.startSchedule();

        scheduledEmailSender.stopSchedule();

        Field field = ScheduledEmailSender.class.getDeclaredField("scheduledTask");
        field.setAccessible(true);
        ScheduledFuture<?> actualScheduledTask = (ScheduledFuture<?>) field.get(scheduledEmailSender);
        assertNull(actualScheduledTask, "Expected the 'scheduledTask' to be null after stopping the schedule");

    }


    @Test
    void testExecuteTask_ProducingEmail() {
        doNothing().when(emailProducer).produce(any(EmailDTO.class));

        scheduledEmailSender.executeTask();

        verify(emailProducer, times(1)).produce(any(EmailDTO.class));
    }

}