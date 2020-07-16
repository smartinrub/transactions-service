package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.TransactionNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionStatusHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionStatusHelper transactionStatusHelper;

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction searchTransaction(String accountIban) {
        return transactionRepository.searchTransaction(accountIban)
                .orElseThrow(() -> new TransactionNotFoundException(accountIban));
    }

    public TransactionStatus getTransactionStatus(UUID reference, Channel channel) {
        Transaction transaction = transactionRepository.findById(reference)
                .orElseThrow(() -> new TransactionNotFoundException(reference));

        return transactionStatusHelper.buildTransactionStatus(transaction, channel);
    }
}
