package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public void createTransaction(Transaction transaction) {

    }

    public Transaction searchTransaction(String accountIban) {
        return null;
    }

    public TransactionStatus getTransactionStatus(String reference, Channel channel) {
        return null;
    }
}
