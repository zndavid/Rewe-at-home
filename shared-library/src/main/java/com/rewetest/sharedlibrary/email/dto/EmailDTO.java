package com.rewetest.sharedlibrary.email.dto;


import jakarta.validation.constraints.Email;
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
public class EmailDTO {

    @NotBlank(message = "Topic is mandatory")
    @Size(max = 255, message = "Topic must not exceed 255 characters")
    private String topic;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @NotBlank(message = "Sender is mandatory")
    @Email(message = "Sender must be a valid email address")
    private String sender;

    @NotEmpty(message = "At least one recipient is mandatory")
    private List<@NotBlank(message = "Each recipient must be a valid email address")
    @Email(message = "Invalid email format") String> recipients;
}
