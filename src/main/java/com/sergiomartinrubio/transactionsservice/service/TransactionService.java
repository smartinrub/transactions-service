package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.TransactionNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

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

        return TransactionStatus.builder()
                .reference(reference)
                .status(Status.PENDING)
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .build();
    }
}
