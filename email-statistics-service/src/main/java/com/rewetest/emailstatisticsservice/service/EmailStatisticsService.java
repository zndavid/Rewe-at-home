package com.rewetest.emailstatisticsservice.service;

import com.rewetest.sharedlibrary.emailstatistics.rto.DomainCountRTO;
import com.rewetest.sharedlibrary.emailstatistics.dto.EmailStatisticsDTO;

import java.util.List;

public interface EmailStatisticsService {
    void create(EmailStatisticsDTO emailStatistics);

    List<DomainCountRTO> getDomainCount();
}