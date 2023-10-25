package com.vsoft.moneytransf.service.impl;

import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.MerchRepostitory;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class MerchantsServiceImplTest {

    public static final String MERCHANT_MAIL = "tester@merchant.com";
    MerchantsServiceImpl tested;

    AutoCloseable openMocks;

    @Mock
    MerchantRepository merchantRepository;

    @Mock
    MerchRepostitory merchRepostitory;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        tested = new MerchantsServiceImpl(merchantRepository, merchRepostitory);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    void importMerchant_invalidStatus_CsvValidationException() throws CsvValidationException, IOException {
        var exception = Assertions.assertThrows(CsvValidationException.class, () ->
          tested.importMerchant(new ByteArrayInputStream("name,description,merchant@merchant.com,ACTIVE\nname,description".getBytes(StandardCharsets.UTF_8)))
        );
        Assertions.assertNotNull(exception);
    }

    @Test
    void importMerchant_incompleteLine_CsvValidationException() throws CsvValidationException, IOException {
        var exception = Assertions.assertThrows(CsvValidationException.class, () ->
                tested.importMerchant(new ByteArrayInputStream("name,description,merchant@merchant.com,ENABLED\nname,description".getBytes(StandardCharsets.UTF_8)))
        );
        Assertions.assertNotNull(exception);
    }

    @Test
    void importMerchant_invalidEmail_CsvValidationException() throws CsvValidationException, IOException {
        var exception = Assertions.assertThrows(CsvValidationException.class, () ->
                tested.importMerchant(new ByteArrayInputStream("name,description,merchant@merchant.com,ENABLED\nname,description,notemail,ENABLED".getBytes(StandardCharsets.UTF_8)))
        );
        Assertions.assertNotNull(exception);
    }

    @Test
    void importMerchant_ok_saved() throws CsvValidationException, IOException {
        tested.importMerchant(new ByteArrayInputStream("name,description,merchant@merchant.com,ENABLED\nsecondName,seconddescription,second_merchant@merchant.com,ENABLED".getBytes(StandardCharsets.UTF_8)));
        Mockito.verify(merchantRepository, times(2)).save(any(Merchant.class));
    }
    @Test
    void listAll() {
    }



    @Test
    void updateByEmail_merchantDTOonlyWithDescription_OnlyDescriptionChanged() {
        Merchant merchant = new Merchant();
        merchant.setDescription("description");
        merchant.setName("merchant");
        merchant.setStatus(MerchantStatus.ENABLED);
        merchant.setEmail(MERCHANT_MAIL);
        Mockito.when(merchantRepository.getByEmail(MERCHANT_MAIL)).thenReturn(merchant);
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setDescription("New description");
        tested.updateByEmail(MERCHANT_MAIL, merchantDTO);
        Assertions.assertEquals(merchantDTO.getDescription(), merchant.getDescription());
        Assertions.assertEquals("merchant", merchant.getName());
    }
}