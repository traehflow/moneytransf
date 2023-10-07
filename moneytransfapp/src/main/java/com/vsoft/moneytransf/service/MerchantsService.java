package com.vsoft.moneytransf.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;

@Service
public class MerchantsService {

    private final MerchantRepository merchantRepository;
    public MerchantsService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public void importMerchant(String csvData) throws CsvValidationException, IOException {
        try (CSVReader reader = new CSVReaderBuilder(new StringReader(csvData)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if(line.length <4) {
                    throw new CsvValidationException("Inconsistent count of elements in the line");
                }
                Merchant merchant = new Merchant();
                merchant.setName(line[0]);
                merchant.setDescription(line[1]);
                merchant.setEmail(line[2]);
                try {
                    merchant.setStatus(MerchantStatus.valueOf(line[3]));
                } catch (IllegalArgumentException e) {
                    throw new CsvValidationException("Invalid Merchant Status");
                }

                merchantRepository.save(merchant);
            }
        }
    }
}
