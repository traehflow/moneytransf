package com.vsoft.moneytransf.service.impl;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.*;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.*;
import com.vsoft.moneytransf.service.TransactionTemplate;
import com.vsoft.moneytransf.service.TransactionsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;

    private final Map<TransactionDescriminator, TransactionTemplate> templates;
    public TransactionsServiceImpl(MerchantRepository merchantRepository,
                                   TransactionTemplate chargeTransactionTemplate,
                                   TransactionTemplate reversalTransactionTemplate,
                                   TransactionTemplate refundTransactionTemplate,
                                   TransactionTemplate authorizeTransactionTemplate, TransactionRepository transactionRepository) {
        this.merchantRepository = merchantRepository;
        this.transactionRepository = transactionRepository;
        templates = Map.of(TransactionDescriminator.CHARGE, chargeTransactionTemplate,
                TransactionDescriminator.REFUND, refundTransactionTemplate,
                TransactionDescriminator.AUTHORIZE, authorizeTransactionTemplate,
                TransactionDescriminator.REVERSAL, reversalTransactionTemplate);
    }

    @Transactional
    public OutputTransactionDTO executeTransaction(InputTransactionDTO inputTransactionDTO, String merchantEmail) {
        Merchant merchant =  merchantRepository.getByEmail(merchantEmail);
        if(merchant == null) {
            throw new MerchantNotFoundException();
        }
        if(merchant.getStatus() == MerchantStatus.DISABLED) {
            throw new InvalidInputDataException("Only enabled merchants can do that transaction.");
        }

        var result = templates.get(inputTransactionDTO.getTransactionType()).execute(inputTransactionDTO, merchant);
        return result;
    }

    @Override
    public List<TransactionDTO> list(TransactionDescriminator descriminator, String userName) {
        Merchant merchant = merchantRepository.getByEmail(userName);
        if(descriminator == null) {
            return transactionRepository.list(merchant);
        } else {
            return transactionRepository.list(descriminator, merchant);
        }
    }
}
