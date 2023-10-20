package com.vsoft.moneytransf.service.impl;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.*;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.*;
import com.vsoft.moneytransf.service.TransactionsService;
import com.vsoft.moneytransf.service.impl.template.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionRepository transactionRepository;

    private final MerchantRepository merchantRepository;

    private final Map<TransactionDescriminator, TransactionTemplate> templates;
    public TransactionsServiceImpl(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
        templates = Map.of(TransactionDescriminator.CHARGE, new ChargeTransactionTemplate(transactionRepository, merchantRepository),
                TransactionDescriminator.REFUND, new RefundTransactionTemplate(transactionRepository, merchantRepository),
                TransactionDescriminator.AUTHORIZE, new AuthorizeTransactionTemplate(transactionRepository, merchantRepository),
                TransactionDescriminator.REVERSAL, new ReversalTransactionTemplate(transactionRepository, merchantRepository));
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
}
