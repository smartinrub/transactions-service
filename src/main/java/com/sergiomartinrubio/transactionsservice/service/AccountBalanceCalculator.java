package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;

import java.math.BigDecimal;

public interface AccountBalanceCalculator {

    BigDecimal calculate(Transaction transaction, Account account);
}
