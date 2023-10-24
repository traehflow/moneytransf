package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.AuthorizeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeTransactionTemplate extends TransactionTemplate {
    public AuthorizeTransactionTemplate(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        super(transactionRepository, merchantRepository);
    }

    @Override
    protected Transaction createTransaction(InputTransactionDTO paymentDTO, Merchant merchant) {
        if(paymentDTO.getAmount() == null) {
            throw new InvalidInputDataException("Amount is required for AUTHORIZE transaction.");
        }
        AuthorizeTransaction authorizeTransaction = new AuthorizeTransaction();
        authorizeTransaction.setAmount(paymentDTO.getAmount());
        authorizeTransaction.setStatus(TransactionStatus.APPROVED);
        return authorizeTransaction;
    }

    @Override
    protected void fillOutputTransactionDTO(Transaction transaction, Merchant merchant, OutputTransactionDTO result) {
        result.setAmount(((AuthorizeTransaction)transaction).getAmount());
    }

    @Override
    protected void updateSums(Merchant merchant, Transaction transaction) {
        
    }
}
