package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class TransactionUtils {

    public BigDecimal getFinalBalance(Transaction transaction, Account account) {
        return account.getBalance().add(transaction.getAmount()).subtract(transaction.getFee());
    }
}
