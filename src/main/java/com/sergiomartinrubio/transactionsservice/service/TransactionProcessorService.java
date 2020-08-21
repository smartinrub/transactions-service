package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.InvalidTransactionException;
import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionProcessorService {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final AccountBalanceCalculator accountBalanceCalculator;

    @Transactional
    public void process(Transaction transaction) {
        checkIfTransactionAlreadyExist(transaction);

        BigDecimal finalBalance = getFinalAccountBalance(transaction);

        if (transaction.getReference() == null) {
            transaction.setReference(UUID.randomUUID());
        }

        if (transaction.getDate() == null) {
            transaction.setDate(ZonedDateTime.now());
        }

        transactionService.save(transaction);
        accountService.updateBalance(finalBalance, transaction.getAccountIban());
    }

    private void checkIfTransactionAlreadyExist(Transaction transaction) {
        Optional<Transaction> savedTransaction = transactionService.findById(transaction.getReference());
        if (savedTransaction.isPresent()) {
            throw new InvalidTransactionException(transaction.getReference());
        }
    }

    private BigDecimal getFinalAccountBalance(Transaction transaction) {
        Account account = accountService.findById(transaction.getAccountIban());
        BigDecimal finalBalance = accountBalanceCalculator.calculate(transaction, account);
        if (finalBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException(finalBalance, transaction.getReference());
        }
        return finalBalance;
    }
}
