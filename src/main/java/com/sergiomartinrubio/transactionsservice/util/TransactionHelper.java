package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class TransactionHelper {

    public BigDecimal getFinalBalance(Transaction transaction, Account account) {
        return account.getBalance()
                .add(transaction.getAmount())
                .subtract(transaction.getFee());
    }

    public List<Transaction> sortTransactionsByAmount(OrderType orderType, List<Transaction> transactions) {
        if (OrderType.DESC == orderType) {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
            Collections.reverse(transactions);
        } else {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
        }
        return transactions;
    }
}
