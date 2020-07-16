package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.TransactionNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction searchTransaction(String accountIban) {
        Optional<Transaction> transaction = transactionRepository.searchTransaction(accountIban);
        if (transaction.isEmpty()) {
            throw new TransactionNotFoundException(accountIban);
        }
        return transaction.get();
    }

    public TransactionStatus getTransactionStatus(UUID reference, Channel channel) {
        return null;
    }
}
