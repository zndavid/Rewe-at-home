package com.rewetest.sharedlibrary.emailstatistics.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailStatisticsDTO {

    @NotBlank(message = "Sender cannot be blank")
    @Size(max = 200, message = "Sender's name must not exceed 200 characters")
    private String sender;

    @NotBlank(message = "Subject cannot be blank")
    @Size(max = 500, message = "Subject must not exceed 500 characters")
    private String subject;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotEmpty(message = "There must be at least one recipient")
    private List<@NotBlank(message = "Recipient email must not be blank") String> recipients;
}
