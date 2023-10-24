package com.vsoft.moneytransf.service.impl;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;
import com.vsoft.moneytransf.service.impl.template.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class TransactionsServiceImplTest {

    public static final String MERCHANT_MAIL = "tester@merchant.com";
    TransactionsServiceImpl tested;

    AutoCloseable openMocks;

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    MerchantRepository merchantRepository;
    @Mock
    ChargeTransactionTemplate chargeTransactionTemplate;
    @Mock
    ReversalTransactionTemplate reversalTransactionTemplate;
    @Mock
    RefundTransactionTemplate refundTransactionTemplatem;
    @Mock
    AuthorizeTransactionTemplate authorizeTransactionTemplate;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void execute_missingMerchant_Exception() {
        tested = new TransactionsServiceImpl(merchantRepository, chargeTransactionTemplate, reversalTransactionTemplate, refundTransactionTemplatem, authorizeTransactionTemplate, transactionRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        Mockito.when(merchantRepository.getByEmail(MERCHANT_MAIL)).thenReturn(null);
        inputTransactionDTO.setTransactionType(TransactionDescriminator.CHARGE);
        var exception = Assertions.assertThrows(MerchantNotFoundException.class, () -> tested.executeTransaction(inputTransactionDTO, MERCHANT_MAIL));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_disabledMerchant_Exception() {
        tested = new TransactionsServiceImpl(merchantRepository, chargeTransactionTemplate, reversalTransactionTemplate, refundTransactionTemplatem, authorizeTransactionTemplate,transactionRepository );
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        Merchant merchant = new Merchant();
        merchant.setName("merchant");
        merchant.setEmail(MERCHANT_MAIL);
        merchant.setStatus(MerchantStatus.DISABLED);
        Mockito.when(merchantRepository.getByEmail(MERCHANT_MAIL)).thenReturn(merchant);
        inputTransactionDTO.setTransactionType(TransactionDescriminator.CHARGE);
        var exception = Assertions.assertThrows(InvalidInputDataException.class, () -> tested.executeTransaction(inputTransactionDTO, MERCHANT_MAIL));
        Assertions.assertNotNull(exception);
    }

    @Test
    void execute_charge_executeChargeTransaction() {
        tested = new TransactionsServiceImpl(merchantRepository, chargeTransactionTemplate, reversalTransactionTemplate, refundTransactionTemplatem, authorizeTransactionTemplate, transactionRepository);
        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        Merchant merchant = new Merchant();
        merchant.setName("merchant");
        merchant.setEmail(MERCHANT_MAIL);
        merchant.setStatus(MerchantStatus.ENABLED);
        Mockito.when(merchantRepository.getByEmail(MERCHANT_MAIL)).thenReturn(merchant);
        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        tested.executeTransaction(inputTransactionDTO, MERCHANT_MAIL);
        Mockito.verify(refundTransactionTemplatem).execute(inputTransactionDTO, merchant);
    }

    @Test
    void list_charge_executeChargeTransaction() {
        tested = new TransactionsServiceImpl(merchantRepository, chargeTransactionTemplate, reversalTransactionTemplate, refundTransactionTemplatem, authorizeTransactionTemplate, transactionRepository);
        Merchant merchant = new Merchant();
        merchant.setName("merchant");
        merchant.setEmail(MERCHANT_MAIL);
        merchant.setStatus(MerchantStatus.ENABLED);
        Mockito.when(merchantRepository.getByEmail(MERCHANT_MAIL)).thenReturn(merchant);
        tested.list(TransactionDescriminator.CHARGE, MERCHANT_MAIL);
        Mockito.verify(transactionRepository).list(TransactionDescriminator.CHARGE, merchant);
    }
}