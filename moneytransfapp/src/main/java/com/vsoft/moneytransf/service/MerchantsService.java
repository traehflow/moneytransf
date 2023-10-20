package com.vsoft.moneytransf.service;

import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.jpl.entity.Merchant;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MerchantsService {
    void importMerchant(String csvData) throws CsvValidationException, IOException;
    List<Merchant> listAll();
    Merchant update(UUID merchantId, MerchantDTO merchantDTO);
}
