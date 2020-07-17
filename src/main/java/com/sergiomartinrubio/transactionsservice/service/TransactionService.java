package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.AccountNotFoundException;
import com.sergiomartinrubio.transactionsservice.exception.InvalidTransactionException;
import com.sergiomartinrubio.transactionsservice.model.*;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionStatusHelper;
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
    private final TransactionStatusHelper transactionStatusHelper;
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

        if (transaction.getDate() == null) {
            transaction.setDate(ZonedDateTime.now());
        }
        transactionRepository.save(transaction);
        accountRepository.updateBalance(finalBalance, transaction.getAccountIban());
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
