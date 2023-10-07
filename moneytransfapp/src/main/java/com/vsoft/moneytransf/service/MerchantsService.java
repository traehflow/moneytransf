package com.vsoft.moneytransf.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.entity.MerchRepostitory;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Service
public class MerchantsService {

    private final MerchantRepository merchantRepository;
    private final MerchRepostitory merchRepostitory;
    public MerchantsService(MerchantRepository merchantRepository, MerchRepostitory merchRepostitory) {
        this.merchantRepository = merchantRepository;
        this.merchRepostitory = merchRepostitory;
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

                try {
                    merchantRepository.save(merchant);
                } catch (DataIntegrityViolationException ex) {
                    throw new InvalidInputDataException(ex);
                }

            }
        }
    }

    public List<Merchant> listAll(){
        return merchRepostitory.findAll();
    }
}
