package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountBalanceCalculator {

    public BigDecimal calculate(Transaction transaction, Account account) {
        return account.getBalance()
                .add(transaction.getAmount())
                .subtract(transaction.getFee());
    }
}
