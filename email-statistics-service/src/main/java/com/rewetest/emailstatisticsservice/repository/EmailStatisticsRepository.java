package com.rewetest.emailstatisticsservice.repository;

import com.rewetest.emailstatisticsservice.entity.EmailStatistics;
import com.rewetest.sharedlibrary.emailstatistics.rto.DomainCountRTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailStatisticsRepository extends JpaRepository<EmailStatistics, Long> {
   @Query("SELECT new com.rewetest.sharedlibrary.emailstatistics.rto.DomainCountRTO(SUBSTRING(e.sender, LOCATE('@', e.sender) + 1) AS domain, COUNT(e) AS count) " +
            "FROM EmailStatistics e " +
            "GROUP BY domain")
    List<DomainCountRTO> countEmailsPerDomain();
}