package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.AccountNotFoundException;
import com.sergiomartinrubio.transactionsservice.exception.InvalidTransactionException;
import com.sergiomartinrubio.transactionsservice.model.*;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionStatusTransformer;
import com.sergiomartinrubio.transactionsservice.util.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionStatusTransformer transactionStatusTransformer;
    private final AccountRepository accountRepository;

    @Transactional
    public void createTransaction(Transaction transaction) {
        if (transactionRepository.findById(transaction.getReference()).isPresent()) {
            throw new InvalidTransactionException(transaction.getReference());
        }

        // we don't want to save transaction for account that do not exist
        Account account = accountRepository.findById(transaction.getAccountIban())
                .orElseThrow(() -> new AccountNotFoundException(transaction.getAccountIban()));

        BigDecimal finalBalance = TransactionUtils.getFinalBalance(transaction, account);
        if (finalBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException(finalBalance, transaction.getReference());
        }

        if (transaction.getReference() == null) {
            transaction.setReference(UUID.randomUUID());
        }

        // we assume that if the date is not set we want to use the current date
        if (transaction.getDate() == null) {
            transaction.setDate(ZonedDateTime.now());
        }
        transactionRepository.save(transaction);
        // we assume we want to update the account balance for every successful transaction
        accountRepository.updateBalance(finalBalance, transaction.getAccountIban());
    }

    public List<Transaction> searchTransaction(String accountIban, OrderType orderType) {
        List<Transaction> transactions = transactionRepository.searchTransaction(accountIban);
        return TransactionUtils.sortTransactionsByAmount(orderType, transactions);
    }

    public TransactionStatus getTransactionStatus(UUID reference, Channel channel) {
        Optional<Transaction> transaction = transactionRepository.findById(reference);
        if (transaction.isEmpty()) {
            return TransactionStatus.builder()
                    .reference(reference)
                    .status(Status.INVALID).build();
        }

        return transactionStatusTransformer.transform(transaction.get(), channel);
    }

}
