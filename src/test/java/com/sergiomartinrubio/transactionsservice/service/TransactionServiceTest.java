package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionStatusHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionStatusHelper transactionStatusHelper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void givenTransactionWhenCreateTransactionThenCallRepositoryToSaveTransaction() {
        // WHEN
        transactionService.createTransaction(TRANSACTION);

        // THEN
        verify(transactionRepository).save(TRANSACTION);
    }

    @Test
    void givenAccountIbanWhenSearchTransactionThenReturnTransaction() {
        // GIVEN
        when(transactionRepository.searchTransaction(ACCOUNT_IBAN)).thenReturn(List.of(TRANSACTION));

        // WHEN
        List<Transaction> result = transactionService.searchTransaction(ACCOUNT_IBAN);

        // THEN
        assertThat(result).containsExactly(TRANSACTION);
    }

    @Test
    void givenReferenceAndChannelWhenGetTransactionStatusThenReturnTransactionStatus() {
        // GIVEN
        TransactionStatus transactionStatus = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.PENDING)
                .amount(AMOUNT)
                .fee(FEE)
                .build();
        when(transactionRepository.findById(TRANSACTION_REFERENCE)).thenReturn(Optional.of(TRANSACTION));
        when(transactionStatusHelper.buildTransactionStatus(TRANSACTION, Channel.CLIENT)).thenReturn(transactionStatus);

        // WHEN
        TransactionStatus result = transactionService.getTransactionStatus(TRANSACTION_REFERENCE, Channel.CLIENT);

        // THEN
        assertThat(result).isEqualTo(transactionStatus);
    }

    @Test
    void givenInvalidReferenceWhenGetTransactionStatusThenStatusIsInvalid() {
        // GIVEN
        when(transactionRepository.findById(TRANSACTION_REFERENCE)).thenReturn(Optional.empty());

        // WHEN
        TransactionStatus result = transactionService.getTransactionStatus(TRANSACTION_REFERENCE, Channel.CLIENT);

        // THEN
        assertThat(result.getStatus()).isEqualTo(Status.INVALID);
    }

}