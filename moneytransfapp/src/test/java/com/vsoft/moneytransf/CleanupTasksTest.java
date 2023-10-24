package com.vsoft.moneytransf;

import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class CleanupTasksTest {

    AutoCloseable openMocks;

    CleanupTasks tested;

    @Mock
    TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        tested = new CleanupTasks(transactionRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    void cleanupTransactions_ok_ok() {
        tested.cleanupTransactions();
        Mockito.verify(transactionRepository).deleteByTimestampBefore(anyLong());
    }
}