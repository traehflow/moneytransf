package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class RefundTransactionTemplateTest {

    RefundTransactionTemplate tested;

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
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setReferencedTransactionId(UUID.randomUUID());
        Merchant merchant = new Merchant();
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_noReferentTransaction_Exception() {
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        Merchant merchant = new Merchant();
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_largerAmountThanCharge_Exception() {
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        UUID referenceTransactionId = UUID.randomUUID();
        inputTransactionDTO.setReferencedTransactionId(referenceTransactionId);
        Merchant merchant = new Merchant();
        merchant.setId(UUID.randomUUID());
        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.setRefundedAmount(new BigDecimal(0));
        chargeTransaction.setAmount(new BigDecimal(500));
        chargeTransaction.setMerchant(merchant);
        chargeTransaction.setId(referenceTransactionId);
        Mockito.when(transactionRepository.fetch(referenceTransactionId)).thenReturn(chargeTransaction);        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_notChargeTransactionReferenced_Exception() {
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        UUID referenceTransactionId = UUID.randomUUID();
        inputTransactionDTO.setReferencedTransactionId(referenceTransactionId);
        Merchant merchant = new Merchant();
        merchant.setId(UUID.randomUUID());
        AuthorizeTransaction authorizeTransaction = new AuthorizeTransaction();
        authorizeTransaction.setAmount(new BigDecimal(500));
        authorizeTransaction.setMerchant(merchant);
        authorizeTransaction.setId(referenceTransactionId);
        Mockito.when(transactionRepository.fetch(referenceTransactionId)).thenReturn(authorizeTransaction);
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_invalidStatusTransactionReferenced_Exception() {
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        UUID referenceTransactionId = UUID.randomUUID();
        inputTransactionDTO.setReferencedTransactionId(referenceTransactionId);
        Merchant merchant = new Merchant();
        merchant.setId(UUID.randomUUID());
        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.setRefundedAmount(new BigDecimal(0));
        chargeTransaction.setAmount(new BigDecimal(1000));
        chargeTransaction.setMerchant(merchant);
        chargeTransaction.setStatus(TransactionStatus.REVERSED);
        chargeTransaction.setId(referenceTransactionId);
        Mockito.when(transactionRepository.fetch(referenceTransactionId)).thenReturn(chargeTransaction);
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_differentMerchant_Exception() {
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        UUID referenceTransactionId = UUID.randomUUID();
        inputTransactionDTO.setReferencedTransactionId(referenceTransactionId);
        Merchant merchant = new Merchant();
        merchant.setId(UUID.randomUUID());
        Merchant merchant1 = new Merchant();
        merchant1.setId(UUID.randomUUID());
        AuthorizeTransaction authorizeTransaction = new AuthorizeTransaction();
        authorizeTransaction.setAmount(new BigDecimal(500));
        authorizeTransaction.setMerchant(merchant);
        authorizeTransaction.setId(referenceTransactionId);
        Mockito.when(transactionRepository.fetch(referenceTransactionId)).thenReturn(authorizeTransaction);
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.execute(inputTransactionDTO, merchant1));
        Assertions.assertNotNull(exception);
    }

    @Test
    void validate_withAmount_ok() {
        tested = new RefundTransactionTemplate(transactionRepository, merchantRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        UUID referenceTransactionId = UUID.randomUUID();
        inputTransactionDTO.setReferencedTransactionId(referenceTransactionId);
        Merchant merchant = new Merchant();
        merchant.setId(UUID.randomUUID());
        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.setRefundedAmount(new BigDecimal(0));
        chargeTransaction.setAmount(new BigDecimal(1000));
        chargeTransaction.setMerchant(merchant);
        chargeTransaction.setId(referenceTransactionId);
        Mockito.when(transactionRepository.fetch(referenceTransactionId)).thenReturn(chargeTransaction);
        var result = tested.execute(inputTransactionDTO, merchant);
        Mockito.verify(transactionRepository).save(any(RefundTransaction.class));
        Assertions.assertEquals(result.getAmount(), new BigDecimal(1000));
        Mockito.verify(merchantRepository).updateMerchantTotalSumBy(any(Merchant.class), eq(new BigDecimal(-1000)));
    }

}