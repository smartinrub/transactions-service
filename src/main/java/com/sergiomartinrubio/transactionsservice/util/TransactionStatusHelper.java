package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import org.springframework.stereotype.Component;

@Component
public class TransactionStatusHelper {

    public TransactionStatus buildTransactionStatus(Transaction transaction, Channel channel) {
        return TransactionStatus.builder().build();
    }
}
