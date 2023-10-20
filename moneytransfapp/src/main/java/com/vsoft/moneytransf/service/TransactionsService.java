package com.vsoft.moneytransf.service;

import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;

public interface TransactionsService {
    OutputTransactionDTO executeTransaction(InputTransactionDTO inputTransactionDTO, String merchantEmail);
}
