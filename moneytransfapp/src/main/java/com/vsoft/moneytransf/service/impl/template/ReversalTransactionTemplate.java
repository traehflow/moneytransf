package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.ReversalTransaction;
import com.vsoft.moneytransf.jpl.entity.Transaction;

public class ReversalTransactionTemplate extends TransactionTemplate{

    public ReversalTransactionTemplate(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        super(transactionRepository, merchantRepository);
    }

    @Override
    protected ReversalTransaction createTransaction(InputTransactionDTO paymentDTO, Merchant merchant) {
        Transaction transaction = transactionRepository.fetch(paymentDTO.getReferencedTransactionId());
        if (transaction.getStatus() == TransactionStatus.APPROVED) {
            transaction.setStatus(TransactionStatus.REVERSED);
            transactionRepository.save(transaction);
        } else {
            throw new InvalidInputDataException("Only transactions in APPROVED stay can be reversed");
        }

        ReversalTransaction reverseTransaction = new ReversalTransaction();
        reverseTransaction.setReferencedTransaction(transaction);
        reverseTransaction.setStatus(TransactionStatus.REVERSED);
        return reverseTransaction;
    }

    @Override
    protected void fillOutputTransactionDTO(Transaction transaction, Merchant merchant, OutputTransactionDTO result) {
        result.setTransactionId(((ReversalTransaction)transaction).getReferencedTransaction().getId());
    }

    @Override
    protected void updateSums(Merchant merchant, Transaction transaction) {
        
    }
}
