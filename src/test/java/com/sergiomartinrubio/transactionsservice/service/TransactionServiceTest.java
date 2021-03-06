package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionSorter;
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
    private TransactionSorter transactionSorter;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void givenTransactionReferenceWhenFindByIdThenReturnTransaction() {
        // GIVEN
        when(transactionRepository.findById(TRANSACTION_REFERENCE)).thenReturn(Optional.of(TRANSACTION));

        // WHEN
        Transaction transaction = transactionService.findById(TRANSACTION_REFERENCE).orElseThrow();

        // THEN
        assertThat(transaction).isEqualTo(TRANSACTION);
    }

    @Test
    void shouldSaveTransaction() {
        // WHEN
        transactionService.save(TRANSACTION);

        // THEN
        verify(transactionRepository).save(TRANSACTION);
    }


    @Test
    void givenAccountIbanWhenSearchTransactionThenReturnTransaction() {
        // GIVEN
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(TRANSACTION);
        when(transactionRepository.searchTransaction(ACCOUNT_IBAN)).thenReturn(transactions);
        when(transactionSorter.sortByAmount(any(OrderType.class), anyList())).thenReturn(List.of(TRANSACTION));

        // WHEN
        List<Transaction> result = transactionService.findByIban(ACCOUNT_IBAN, OrderType.ASC);

        // THEN
        assertThat(result).containsExactly(TRANSACTION);
    }

}