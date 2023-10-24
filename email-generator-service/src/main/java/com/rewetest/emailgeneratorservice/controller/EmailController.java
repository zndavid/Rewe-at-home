package com.rewetest.emailgeneratorservice.controller;

import com.rewetest.emailgeneratorservice.kafka.EmailProducer;
import com.rewetest.emailgeneratorservice.schedule.ScheduledEmailSender;
import com.rewetest.sharedlibrary.email.dto.EmailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Email Controller", description = "The Email API for generating and scheduling emails")
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailProducer emailProducer;
    private final ScheduledEmailSender scheduledEmailSender;

    @Operation(summary = "Generate an email", description = "Accepts an email request and places it in the processing queue.")
    @ApiResponse(responseCode = "202", description = "Email request accepted",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))})
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@Validated @RequestBody EmailDTO emailDTO) {
        emailProducer.produce(emailDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email request accepted!");
    }

    @Operation(summary = "Start Email Schedule", description = "Start the scheduled task of sending emails")
    @ApiResponse(responseCode = "202", description = "Scheduled task started",
            content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/schedule/start")
    public ResponseEntity<String> startSchedule() {
        scheduledEmailSender.startSchedule();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Scheduled task started. It will execute every 2 seconds.");
    }

    @Operation(summary = "Stop Email Schedule", description = "Stop the scheduled task of sending emails")
    @ApiResponse(responseCode = "202", description = "Scheduled task stopped",
            content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/schedule/stop")
    public ResponseEntity<String> stopSchedule() {
        scheduledEmailSender.stopSchedule();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Scheduled task stopped.");
    }

}
