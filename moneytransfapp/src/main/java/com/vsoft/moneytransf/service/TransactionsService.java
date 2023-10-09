package com.vsoft.moneytransf.service;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.TransactionStatus;
import com.vsoft.moneytransf.dto.PaymentDto;
import com.vsoft.moneytransf.dto.PaymentResultDTO;
import com.vsoft.moneytransf.dto.ReversePaymentDTO;
import com.vsoft.moneytransf.dto.TransactionDTO;
import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.AuthorizeTransaction;
import com.vsoft.moneytransf.jpl.entity.ChargeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionsService {
    private final TransactionRepository transactionRepository;

    private final MerchantRepository merchantRepository;

    public TransactionsService(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
    }

    @Transactional
    public PaymentResultDTO executePayment(PaymentDto paymentDto) {
        Merchant merchant =  merchantRepository.getByEmail(paymentDto.getMerchantEmali());
        if(merchant.getStatus() == MerchantStatus.DISABLED) {
            throw new InvalidInputDataException("Only enabled merchants can do that transaction.");
        }
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

    @Transactional
    public PaymentResultDTO hold(PaymentDto paymentDto) {
        Merchant merchant =  merchantRepository.getByEmail(paymentDto.getMerchantEmali());
        if(merchant.getStatus() == MerchantStatus.DISABLED) {
            throw new InvalidInputDataException("Only enabled merchants can do that transaction.");
        }
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

    @Transactional
    public TransactionDTO reversePayment(ReversePaymentDTO reversePaymentDTO) {
        Transaction transaction = transactionRepository.fetch(reversePaymentDTO.getTransactionId());
        if (transaction.getStatus() == TransactionStatus.APPROVED) {
            transaction.setStatus(TransactionStatus.REVERSED);
            transactionRepository.save(transaction);
        } else {
            throw new InvalidInputDataException("Only transactions in APPROVED stay can be reversed");
        }

        Merchant merchant = transaction.getMerchant();
        if(merchant.getStatus() == MerchantStatus.DISABLED) {
            throw new InvalidInputDataException("Only enabled merchants can do that transaction.");
        }
        Transaction reverseTransaction = new Transaction();
        reverseTransaction.setCustomerEmail(transaction.getCustomerEmail());
        reverseTransaction.setCustomerPhone(transaction.getCustomerPhone());
        reverseTransaction.setReferencedTransaction(transaction);
        reverseTransaction.setStatus(TransactionStatus.REVERSED);
        transactionRepository.save(reverseTransaction);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTransactionId(reverseTransaction.getId());
        transactionDTO.setCustomerEmail(transaction.getCustomerEmail());
        transactionDTO.setCustomerPhone(transaction.getCustomerPhone());
        return transactionDTO;
    }

}
