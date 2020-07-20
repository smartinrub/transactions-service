package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;

import java.util.List;

public interface TransactionService {

    void createTransaction(Transaction transaction);

    List<Transaction> searchTransaction(String accountIban, OrderType orderType);

}
