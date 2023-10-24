package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.Transaction;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

class AuthorizeTransactionTemplateTest {


    AuthorizeTransactionTemplate tested;

    AutoCloseable openMocks;

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    MerchantRepository merchantRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void execute_noAmount_Exception() {
        tested = new AuthorizeTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.CHARGE);
        Merchant merchant = new Merchant();
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant));
        Assertions.assertNotNull(exception);
    }

    @Test
    void validate_withAmount_ok() {
        tested = new AuthorizeTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.CHARGE);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        Merchant merchant = new Merchant();
        var result = tested.execute(inputTransactionDTO, merchant);
        Mockito.verify(transactionRepository).save(any(Transaction.class));
        Assertions.assertEquals(result.getAmount(), new BigDecimal(1000));
        Mockito.verify(merchantRepository, Mockito.never()).updateMerchantTotalSumBy(any(Merchant.class), any(BigDecimal.class));
    }

}