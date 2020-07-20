package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionSorterTest {

    private static final UUID TRANSACTION_REFERENCE = UUID.randomUUID();
    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42.000Z");
    private static final BigDecimal AMOUNT_1 = new BigDecimal("195.38");
    private static final BigDecimal AMOUNT_2 = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");

    private static final Transaction TRANSACTION_1 = Transaction.builder()
            .reference(TRANSACTION_REFERENCE)
            .accountIban(ACCOUNT_IBAN)
            .date(DATE)
            .amount(AMOUNT_1)
            .fee(FEE)
            .description("Restaurant payment")
            .build();

    private static final Transaction TRANSACTION_2 = Transaction.builder()
            .reference(TRANSACTION_REFERENCE)
            .accountIban(ACCOUNT_IBAN)
            .date(DATE)
            .amount(AMOUNT_2)
            .fee(FEE)
            .description("Restaurant payment")
            .build();

    private TransactionSorter transactionSorter = new TransactionSorter();

    @Test
    void givenAscOrderWhenSortTransactionsThenReturnedTransactionsInAscOrder() {
        // GIVEN
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(TRANSACTION_1);
        transactions.add(TRANSACTION_2);

        // WHEN
        List<Transaction> sortedTransactions = transactionSorter.sortByAmount(OrderType.ASC, transactions);

        // THEN
        assertThat(sortedTransactions).containsExactly(TRANSACTION_2, TRANSACTION_1);
    }

    @Test
    void givenAscOrderWhenSortTransactionsThenReturnedTransactionsInDescOrder() {
        // GIVEN
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(TRANSACTION_1);
        transactions.add(TRANSACTION_2);

        // WHEN
        List<Transaction> sortedTransactions = transactionSorter.sortByAmount(OrderType.DESC, transactions);

        // THEN
        assertThat(sortedTransactions).containsExactly(TRANSACTION_1, TRANSACTION_2);
    }

    @Test
    void givenNoOrderWhenSortTransactionsThenReturnedTransactionsInAscOrder() {
        // GIVEN
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(TRANSACTION_1);
        transactions.add(TRANSACTION_2);

        // WHEN
        List<Transaction> sortedTransactions = transactionSorter.sortByAmount(null, transactions);

        // THEN
        assertThat(sortedTransactions).containsExactly(TRANSACTION_2, TRANSACTION_1);
    }

}