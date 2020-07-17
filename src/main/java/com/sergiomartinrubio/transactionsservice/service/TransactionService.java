package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionStatusHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionStatusHelper transactionStatusHelper;

    public void createTransaction(Transaction transaction) {
        if (transaction.getReference() == null) {
            transaction.setReference(UUID.randomUUID());
        }
        if (transaction.getDate() == null) {
            transaction.setDate(ZonedDateTime.now());
        }
        transactionRepository.save(transaction);
    }

    public List<Transaction> searchTransaction(String accountIban) {
        return transactionRepository.searchTransaction(accountIban);
    }

    public TransactionStatus getTransactionStatus(UUID reference, Channel channel) {
        Optional<Transaction> transaction = transactionRepository.findById(reference);
        if (transaction.isEmpty()) {
            return TransactionStatus.builder()
                    .reference(reference)
                    .status(Status.INVALID).build();
        }

        return transactionStatusHelper.buildTransactionStatus(transaction.get(), channel);
    }
}
