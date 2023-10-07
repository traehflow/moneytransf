package com.vsoft.moneytransf.service;

import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.PaymentDto;
import com.vsoft.moneytransf.dto.PaymentResultDTO;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.AuthorizeTransaction;
import com.vsoft.moneytransf.jpl.entity.ChargeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {
    private final TransactionRepository transactionRepository;

    private final MerchantRepository merchantRepository;

    public TransactionsService(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
    }

    public PaymentResultDTO executePayment(PaymentDto paymentDto) {
        Merchant merchant =  merchantRepository.getById(paymentDto.getCustomerId());
        System.out.println(merchant.getName());
        AuthorizeTransaction authorizeTransaction = new AuthorizeTransaction();
        authorizeTransaction.setMerchant(merchant);
        authorizeTransaction.setAmount(paymentDto.getAmount());
        authorizeTransaction.setCustomerEmail(paymentDto.getCustomerEmail());
        authorizeTransaction.setCustomerPhone(paymentDto.getCustomerPhone());
        authorizeTransaction.setStatus(TransactionStatus.APPROVED);
        transactionRepository.save(authorizeTransaction);

        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.setMerchant(merchant);
        chargeTransaction.setAmount(paymentDto.getAmount());
        chargeTransaction.setCustomerEmail(paymentDto.getCustomerEmail());
        chargeTransaction.setReferencedTransaction(authorizeTransaction);
        transactionRepository.save(chargeTransaction);

        PaymentResultDTO paymentResultDTO = new PaymentResultDTO();
        paymentResultDTO.setAuthorizeTransactionId(authorizeTransaction.getId());
        paymentResultDTO.setChargeTransactionId(chargeTransaction.getId());
        paymentResultDTO.setAmount(authorizeTransaction.getAmount());

        return paymentResultDTO;
    }

    public PaymentResultDTO hold(PaymentDto paymentDto) {
        Merchant merchant =  merchantRepository.getById(paymentDto.getCustomerId());
        System.out.println(merchant.getName());
        AuthorizeTransaction authorizeTransaction = new AuthorizeTransaction();
        authorizeTransaction.setMerchant(merchant);
        authorizeTransaction.setAmount(paymentDto.getAmount());
        authorizeTransaction.setCustomerEmail(paymentDto.getCustomerEmail());
        authorizeTransaction.setCustomerPhone(paymentDto.getCustomerPhone());
        transactionRepository.save(authorizeTransaction);

        PaymentResultDTO paymentResultDTO = new PaymentResultDTO();
        paymentResultDTO.setAuthorizeTransactionId(authorizeTransaction.getId());
        paymentResultDTO.setAmount(authorizeTransaction.getAmount());
        return paymentResultDTO;
    }
}
