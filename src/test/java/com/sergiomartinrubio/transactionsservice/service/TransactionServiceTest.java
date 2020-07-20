package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.AccountNotFoundException;
import com.sergiomartinrubio.transactionsservice.exception.InvalidTransactionException;
import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.service.impl.TransactionServiceImpl;
import com.sergiomartinrubio.transactionsservice.util.TransactionHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final UUID TRANSACTION_REFERENCE = UUID.randomUUID();
    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42.000Z");
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final Transaction TRANSACTION = Transaction.builder()
            .reference(TRANSACTION_REFERENCE)
            .accountIban(ACCOUNT_IBAN)
            .date(DATE)
            .amount(AMOUNT)
            .fee(FEE)
            .description("Restaurant payment")
            .build();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionHelper transactionHelper;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void givenTransactionWhenCreateTransactionThenCallRepositoryToSaveTransaction() {
        // GIVEN
        Account account = new Account(ACCOUNT_IBAN, BigDecimal.valueOf(100L));
        when(accountRepository.findById(ACCOUNT_IBAN))
                .thenReturn(Optional.of(account));
        when(transactionHelper.getFinalBalance(TRANSACTION, account)).thenReturn(new BigDecimal("293.38"));

        // WHEN
        transactionService.createTransaction(TRANSACTION);

        // THEN
        verify(transactionRepository).save(TRANSACTION);
    }

    @Test
    void givenAccountNotFoundWhenCreateTransactionThenThrowAccountNotFoundException() {
        // GIVEN
        when(transactionRepository.findById(TRANSACTION_REFERENCE)).thenReturn(Optional.empty());
        when(accountRepository.findById(ACCOUNT_IBAN))
                .thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThatThrownBy(() -> transactionService.createTransaction(TRANSACTION))
                .isInstanceOf(AccountNotFoundException.class);
        verify(transactionRepository).findById(TRANSACTION_REFERENCE);
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void givenFinalBalanceBelowZeroWhenCreateTransactionThenThrowInvalidTransactionException() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE)
                .amount(BigDecimal.valueOf(-250))
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        Account account = new Account(ACCOUNT_IBAN, BigDecimal.valueOf(200L));
        when(transactionRepository.findById(TRANSACTION_REFERENCE)).thenReturn(Optional.empty());
        when(accountRepository.findById(ACCOUNT_IBAN))
                .thenReturn(Optional.of(account));
        when(transactionHelper.getFinalBalance(transaction, account)).thenReturn(new BigDecimal(-50));

        // WHEN
        // THEN
        assertThatThrownBy(() -> transactionService.createTransaction(transaction))
                .isInstanceOf(InvalidTransactionException.class);
        verify(transactionRepository).findById(TRANSACTION_REFERENCE);
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void givenAccountIbanWhenSearchTransactionThenReturnTransaction() {
        // GIVEN
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(TRANSACTION);
        when(transactionRepository.searchTransaction(ACCOUNT_IBAN)).thenReturn(transactions);
        when(transactionHelper.sortTransactionsByAmount(any(OrderType.class), anyList())).thenReturn(List.of(TRANSACTION));

        // WHEN
        List<Transaction> result = transactionService.searchTransaction(ACCOUNT_IBAN, OrderType.ASC);

        // THEN
        assertThat(result).containsExactly(TRANSACTION);
    }

}