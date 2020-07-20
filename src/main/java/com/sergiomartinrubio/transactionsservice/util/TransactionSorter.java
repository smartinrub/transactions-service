package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class TransactionSorter {

    public List<Transaction> sortByAmount(OrderType orderType, List<Transaction> transactions) {
        if (OrderType.DESC == orderType) {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
            Collections.reverse(transactions);
        } else {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
        }
        return transactions;
    }
}
