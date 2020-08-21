package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionSorter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionSorter transactionSorter;

    public Optional<Transaction> findById(UUID reference) {
        return transactionRepository.findById(reference);
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> findByIban(String accountIban, OrderType orderType) {
        List<Transaction> transactions = transactionRepository.searchTransaction(accountIban);
        return transactionSorter.sortByAmount(orderType, transactions);
    }

}
