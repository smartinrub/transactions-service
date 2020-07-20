package com.sergiomartinrubio.transactionsservice.service.impl;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.service.AccountBalanceCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountBalanceCalculatorImpl implements AccountBalanceCalculator {

    @Override
    public BigDecimal calculate(Transaction transaction, Account account) {
        return account.getBalance()
                .add(transaction.getAmount())
                .subtract(transaction.getFee());
    }
}
