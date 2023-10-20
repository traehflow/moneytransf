package com.vsoft.moneytransf.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.entity.MerchRepostitory;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.service.MerchantsService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

@Service
public class MerchantsServiceImpl implements MerchantsService {

    private final MerchantRepository merchantRepository;
    private final MerchRepostitory merchRepostitory;
    public MerchantsServiceImpl(MerchantRepository merchantRepository, MerchRepostitory merchRepostitory) {
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

    public Merchant update(UUID merchantId, MerchantDTO merchantDTO) {
        Merchant merchant = merchantRepository.getById(merchantId);
        if(merchantDTO.getDescription() != null) {
            merchant.setDescription(merchantDTO.getDescription());
        }
        if(merchantDTO.getEmail() != null) {
            merchant.setEmail(merchantDTO.getEmail());
        }
        if(merchantDTO.getName() != null) {
            merchant.setName(merchantDTO.getName());
        }
        if(merchantDTO.getStatus() != null) {
            merchant.setStatus(merchantDTO.getStatus());
        }
        return merchRepostitory.save(merchant);

    }
}
