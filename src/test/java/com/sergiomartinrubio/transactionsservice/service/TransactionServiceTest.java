package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.TransactionNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final UUID TRANSACTION_REFERENCE = UUID.randomUUID();
    public static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42.000Z");

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void givenTransactionWhenCreateTransactionThenCallRepositoryToSaveTransaction() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE)
                .amount(new BigDecimal("193.38"))
                .fee(new BigDecimal("3.18"))
                .description("Restaurant payment")
                .build();

        // WHEN
        transactionService.createTransaction(transaction);

        // THEN
        verify(transactionRepository).save(transaction);
    }

    @Test
    void givenAccountIbanWhenSearchTransactionThenReturnTransaction() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE)
                .amount(new BigDecimal("193.38"))
                .fee(new BigDecimal("3.18"))
                .description("Restaurant payment")
                .build();
        when(transactionRepository.searchTransaction(ACCOUNT_IBAN)).thenReturn(Optional.of(transaction));

        // WHEN
        Transaction result = transactionService.searchTransaction(ACCOUNT_IBAN);

        // THEN
        assertThat(result).isEqualTo(transaction);
    }

    @Test
    void givenInvalidAccountIbanWhenSearchTransactionThenThrowTransactionNotFoundException() {
        // GIVEN
        when(transactionRepository.searchTransaction(ACCOUNT_IBAN)).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThatThrownBy(() -> transactionService.searchTransaction(ACCOUNT_IBAN))
                .isInstanceOf(TransactionNotFoundException.class);
    }

}