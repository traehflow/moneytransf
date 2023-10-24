package com.vsoft.moneytransf.service;

import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.dto.TransactionDTO;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;

import java.util.List;

public interface TransactionsService {
    OutputTransactionDTO executeTransaction(InputTransactionDTO inputTransactionDTO, String merchantEmail);

    List<TransactionDTO> list(TransactionDescriminator descriminator, String userName);
}
