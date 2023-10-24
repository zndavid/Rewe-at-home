package com.rewetest.emailstatisticsservice.service;

import com.rewetest.emailstatisticsservice.entity.EmailStatistics;
import com.rewetest.emailstatisticsservice.repository.EmailStatisticsRepository;
import com.rewetest.sharedlibrary.emailstatistics.rto.DomainCountRTO;
import com.rewetest.sharedlibrary.emailstatistics.dto.EmailStatisticsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailStatisticsServiceImpl implements EmailStatisticsService {

    private final EmailStatisticsRepository emailStatisticsRepository;

    public void create(EmailStatisticsDTO emailStatisticsDTO) {
        log.info("Creating email statistics record for sender: {}", emailStatisticsDTO.getSender());
        try {
            EmailStatistics emailStatistics = mapDtoToEntity(emailStatisticsDTO);
            emailStatisticsRepository.save(emailStatistics);
            log.info("Successfully created email statistics record");
        } catch (Exception e) {
            log.error("Error occurred while creating email statistics record for sender: " + emailStatisticsDTO.getSender(), e);
                //TODO ADD ERROR HANDLING HERE AND IMPLEMENT GLOBAL EXCEPTION HANDLER

        }
    }

    public List<DomainCountRTO> getDomainCount() {
        log.info("Retrieving count of emails per domain");
        try {
            List<DomainCountRTO> domainCounts = this.emailStatisticsRepository.countEmailsPerDomain();
            log.info("Retrieved domain counts: {}", domainCounts);
            return domainCounts;
        } catch (Exception e) {
            log.error("Error occurred while retrieving domain counts", e);
            return null;
            //TODO ADD ERROR HANDLING HERE AND IMPLEMENT GLOBAL EXCEPTION HANDLER

        }
    }

    private EmailStatistics mapDtoToEntity(EmailStatisticsDTO dto) {
        log.debug("Mapping DTO to EmailStatistics entity for sender: {}", dto.getSender());
        return EmailStatistics.builder()
                .sender(dto.getSender())
                .subject(dto.getSubject())
                .content(dto.getContent())
                .recipients(dto.getRecipients())
                .build();
    }
}
