package com.vsoft.moneytransf;

import com.vsoft.moneytransf.jpl.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CleanupTasks {
    @Autowired
    private TransactionRepository transactionRepository;

    @Scheduled(cron = "0 */5 * * * *") // Executes every 5 minutes
    public void cleanupTransactions() {

        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        Instant oneHourEarlier = instant.minusSeconds(3600);

        transactionRepository.deleteByTimestampBefore(oneHourEarlier.toEpochMilli());

    }
}
