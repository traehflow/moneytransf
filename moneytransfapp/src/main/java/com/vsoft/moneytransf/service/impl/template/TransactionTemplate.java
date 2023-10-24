package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.Transaction;

public abstract class TransactionTemplate {

    MerchantRepository merchantRepository;
    TransactionRepository transactionRepository;

    public TransactionTemplate(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
    }
    public OutputTransactionDTO execute(InputTransactionDTO inputTransactionDTO, Merchant merchant) {
        Transaction transaction = createTransaction(inputTransactionDTO, merchant);
        transaction.setMerchant(merchantRepository.getById(merchant.getId()));
        transaction.setCustomerPhone(inputTransactionDTO.getCustomerPhone());
        transaction.setCustomerEmail(inputTransactionDTO.getCustomerEmail());
        transactionRepository.save(transaction);

        updateSums(merchant, transaction);

        OutputTransactionDTO resultDTO = new OutputTransactionDTO();
        resultDTO.setTransactionId(transaction.getId());

        fillOutputTransactionDTO(transaction, merchant, resultDTO);
        return resultDTO;

    }

    protected abstract Transaction createTransaction(InputTransactionDTO paymentDTO, Merchant merchant);

    protected abstract void fillOutputTransactionDTO(Transaction transaction, Merchant merchant, OutputTransactionDTO result);

    protected abstract void updateSums(Merchant merchant, Transaction transaction);
}
