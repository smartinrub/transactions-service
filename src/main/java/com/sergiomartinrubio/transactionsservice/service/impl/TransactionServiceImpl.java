package com.sergiomartinrubio.transactionsservice.service.impl;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import com.sergiomartinrubio.transactionsservice.util.TransactionSorter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionSorter transactionSorter;

    @Override
    public Optional<Transaction> findById(UUID reference) {
        return transactionRepository.findById(reference);
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByIban(String accountIban, OrderType orderType) {
        List<Transaction> transactions = transactionRepository.searchTransaction(accountIban);
        return transactionSorter.sortByAmount(orderType, transactions);
    }

}
