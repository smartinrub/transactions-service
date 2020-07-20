package com.sergiomartinrubio.transactionsservice.service.impl;

import com.sergiomartinrubio.transactionsservice.exception.InvalidTransactionException;
import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.service.AccountBalanceCalculator;
import com.sergiomartinrubio.transactionsservice.service.AccountService;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionProcessorServiceImplTest {

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
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountBalanceCalculator accountBalanceCalculator;

    @InjectMocks
    private TransactionProcessorServiceImpl transactionProcessorService;

    @Test
    void givenTransactionWhenCreateTransactionThenCallRepositoryToSaveTransaction() {
        // GIVEN
        Account account = new Account(ACCOUNT_IBAN, BigDecimal.valueOf(100L));
        when(accountService.findById(ACCOUNT_IBAN)).thenReturn(account);
        when(accountBalanceCalculator.calculate(TRANSACTION, account)).thenReturn(new BigDecimal("293.38"));

        // WHEN
        transactionProcessorService.process(TRANSACTION);

        // THEN
        verify(transactionService).save(TRANSACTION);
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
        when(transactionService.findById(TRANSACTION_REFERENCE)).thenReturn(Optional.empty());
        when(accountService.findById(ACCOUNT_IBAN)).thenReturn(account);
        when(accountBalanceCalculator.calculate(transaction, account)).thenReturn(new BigDecimal(-50));

        // WHEN
        // THEN
        assertThatThrownBy(() -> transactionProcessorService.process(transaction))
                .isInstanceOf(InvalidTransactionException.class);
        verify(transactionService).findById(TRANSACTION_REFERENCE);
        verifyNoMoreInteractions(transactionService);
    }


}