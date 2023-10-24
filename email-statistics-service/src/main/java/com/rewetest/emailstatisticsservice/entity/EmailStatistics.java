package com.rewetest.emailstatisticsservice.entity;


import com.rewetest.emailstatisticsservice.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class EmailStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String sender;

    @Column(nullable = false, length = 500)
    private String subject;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> recipients = new ArrayList<>();
}
