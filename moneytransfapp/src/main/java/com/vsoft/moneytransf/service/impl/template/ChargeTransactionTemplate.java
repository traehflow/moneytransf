package com.vsoft.moneytransf.service.impl.template;

import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.ChargeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.Transaction;

public class ChargeTransactionTemplate extends TransactionTemplate {
    public ChargeTransactionTemplate(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        super(transactionRepository, merchantRepository);
    }

    @Override
    protected ChargeTransaction createTransaction(InputTransactionDTO paymentDTO, Merchant merchant) {
        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.setAmount(paymentDTO.getAmount());
        chargeTransaction.setStatus(TransactionStatus.APPROVED);
        return chargeTransaction;
    }

    @Override
    protected void fillOutputTransactionDTO(Transaction transaction, Merchant merchant, OutputTransactionDTO result) {
        result.setAmount(((ChargeTransaction)transaction).getAmount());
    }

    @Override
    protected void updateSums(Merchant merchant, Transaction transaction) {
        merchantRepository.updateMerchantTotalSumBy(merchant, ((ChargeTransaction)transaction).getAmount());

    }
}
