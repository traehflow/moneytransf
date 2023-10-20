package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.ChargeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.RefundTransaction;
import com.vsoft.moneytransf.jpl.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RefundTransactionTemplate extends TransactionTemplate{
    public RefundTransactionTemplate(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        super(transactionRepository, merchantRepository);
    }

    @Override
    protected Transaction createTransaction(InputTransactionDTO paymentDTO, Merchant merchant) {
        Transaction transaction = transactionRepository.fetch(paymentDTO.getReferencedTransactionId());
        if(!(transaction instanceof ChargeTransaction)) {
            throw new InvalidInputDataException("Only CHARGE transaction can be REFUNDED.");
        }
        if (transaction.getStatus() != TransactionStatus.REVERSED) {
            transaction.setStatus(TransactionStatus.REFUNDED);
            transactionRepository.save(transaction);
        } else {
            throw new InvalidInputDataException("REVERSED state transactions cannot be REFUNDED.");
        }

        RefundTransaction refundTransaction = new RefundTransaction();
        refundTransaction.setAmount(paymentDTO.getAmount());
        refundTransaction.setReferencedTransaction(transaction);
        refundTransaction.setStatus(TransactionStatus.REFUNDED);
        return refundTransaction;
    }

    @Override
    protected void fillOutputTransactionDTO(Transaction transaction, Merchant merchant, OutputTransactionDTO result) {
        result.setAmount(((RefundTransaction)transaction).getAmount());
        result.setTransactionId(((RefundTransaction)transaction).getReferencedTransaction().getId());

    }

    @Override
    protected void updateSums(Merchant merchant, Transaction transaction) {
        merchantRepository.updateMerchantTotalSumBy(merchant, ((RefundTransaction)transaction).getAmount().multiply(new BigDecimal(-1)));
    }
}
