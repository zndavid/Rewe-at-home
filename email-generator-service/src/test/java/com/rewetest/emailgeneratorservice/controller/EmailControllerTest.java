package com.rewetest.emailgeneratorservice.controller;

import com.rewetest.emailgeneratorservice.kafka.EmailProducer;
import com.rewetest.emailgeneratorservice.schedule.ScheduledEmailSender;
import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailProducer emailProducer;

    @Mock
    private ScheduledEmailSender scheduledEmailSender;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    public void generateEmail_ValidEmail_SendsMessage() throws Exception {
        String emailJson = "{" +
                "\"topic\": \"Test\"," +
                "\"content\": \"This is a test email.\"," +
                "\"sender\": \"sender@example.com\"," +
                "\"recipients\": [\"recipient@example.com\"]" +
                "}";

        EmailDTO emailDTO = new EmailDTO(
                "Test",
                "This is a test email.",
                "sender@example.com",
                Collections.singletonList("recipient@example.com")
        );

        mockMvc.perform(post("/api/v1/email/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Email request accepted!"));

        verify(emailProducer, times(1)).produce(emailDTO);
    }

    @Test
    public void startSchedule_StartsScheduledTask() throws Exception {
        mockMvc.perform(get("/api/v1/email/schedule/start"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Scheduled task started. It will execute every 2 seconds."));

        verify(scheduledEmailSender, times(1)).startSchedule();
    }

    @Test
    public void stopSchedule_StopsScheduledTask() throws Exception {
        mockMvc.perform(get("/api/v1/email/schedule/stop"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Scheduled task stopped."));

        verify(scheduledEmailSender, times(1)).stopSchedule();
    }
}
