package com.rewetest.emailstatisticsservice.controller;

import com.rewetest.emailstatisticsservice.service.EmailStatisticsService;
import com.rewetest.sharedlibrary.emailstatistics.rto.DomainCountRTO;
import com.rewetest.sharedlibrary.emailstatistics.dto.EmailStatisticsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Email Statistics Controller", description = "Operations pertaining to email statistics in the application")
@RequestMapping("api/v1/statistics")
@Slf4j
@RestController()
public class EmailStatisticsController {

    private final EmailStatisticsService emailStatisticsService;

    @Operation(summary = "Create Email Statistics", description = "Create a new record of email statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Statistics created successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = EmailStatisticsDTO.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@Valid @RequestBody EmailStatisticsDTO emailStatistics) {
        emailStatisticsService.create(emailStatistics);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get Domain Count", description = "Retrieve email statistics based on domain count")
    @ApiResponse(responseCode = "200", description = "List of domain counts retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DomainCountRTO.class))))
    @GetMapping
    public ResponseEntity<List<DomainCountRTO>> getDomainCount(){
        List<DomainCountRTO> domainCounts = emailStatisticsService.getDomainCount();
        return new ResponseEntity<>(domainCounts, HttpStatus.OK);
    }
}
