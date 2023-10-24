package com.vsoft.moneytransf.service;

import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.jpl.entity.Merchant;

public interface TransactionTemplate {
    OutputTransactionDTO execute(InputTransactionDTO inputTransactionDTO, Merchant merchant);
}
