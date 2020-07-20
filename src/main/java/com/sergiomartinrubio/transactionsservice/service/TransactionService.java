package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {

    Optional<Transaction> findById(UUID reference);

    void save(Transaction transaction);

    List<Transaction> findByIban(String accountIban, OrderType orderType);

}
