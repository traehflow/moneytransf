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

@Component("refundTransactionTemplate")
public class RefundTransactionTemplate extends TransactionTemplateImpl {
    public RefundTransactionTemplate(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        super(transactionRepository, merchantRepository);
    }

    @Override
    protected Transaction createTransaction(InputTransactionDTO paymentDTO, Merchant merchant) {
        if(paymentDTO.getReferencedTransactionId() == null) {
            throw new InvalidInputDataException("Reference ID is required for REFUND transaction.");
        }
        if(paymentDTO.getAmount() == null) {
            throw new InvalidInputDataException("Amount is required for REFUND transaction.");
        }

        Transaction transaction = transactionRepository.fetch(paymentDTO.getReferencedTransactionId());

        if(transaction == null || transaction.getMerchant().getId() != merchant.getId()) {
            throw new InvalidInputDataException("Invalid referenced transaction ID.");
        }

        if(!(transaction instanceof ChargeTransaction chargeTransaction)) {
            throw new InvalidInputDataException("Only CHARGE transaction can be REFUNDED.");
        }
        if(chargeTransaction.getAmount().compareTo(paymentDTO.getAmount().add(chargeTransaction.getRefundedAmount())) < 0) {
            throw new InvalidInputDataException("REFUNDED amount cannot be larger than the amount of the referenced transaction plus refundend amount of previous transactions.");
        }
        if (chargeTransaction.getStatus() != TransactionStatus.REVERSED) {
            transactionRepository.updateRefundedChargeTransaction(chargeTransaction, paymentDTO.getAmount());
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
