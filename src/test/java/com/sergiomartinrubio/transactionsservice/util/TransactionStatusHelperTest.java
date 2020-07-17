package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.sergiomartinrubio.transactionsservice.model.Channel.*;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionStatusHelperTest {

    private static final UUID TRANSACTION_REFERENCE = UUID.randomUUID();
    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");

    @Test
    public void givenClientOrAtmChannelAndTransactionBeforeTodayWhenBuildTransactionStatusThenReturnSettledAndAmountSubstractingFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, CLIENT);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.SETTLED)
                .amount(AMOUNT.subtract(FEE))
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

    @Test
    public void givenInternalChannelAndTransactionBeforeTodayWhenBuildTransactionStatusThenReturnSettledAndAmountAndFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, INTERNAL);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.SETTLED)
                .amount(AMOUNT)
                .fee(FEE)
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

    @Test
    public void givenClientOrAtmChannelAndTransactionTodayWhenBuildTransactionStatusThenReturnPendingAndAmountSubstractingFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now())
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, CLIENT);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.PENDING)
                .amount(AMOUNT.subtract(FEE))
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

    @Test
    public void givenInternalChannelAndTransactionTodayWhenBuildTransactionStatusThenReturnPendingAndAmountAndFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now())
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, INTERNAL);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.PENDING)
                .amount(AMOUNT)
                .fee(FEE)
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

    @Test
    public void givenClientChannelAndTransactionGreaterThanTodayWhenBuildTransactionStatusThenReturnFutureAndAmountSubstractingFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now().plus(1, ChronoUnit.DAYS))
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, CLIENT);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.FUTURE)
                .amount(AMOUNT.subtract(FEE))
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

    @Test
    public void givenAtmChannelAndTransactionGreaterThanTodayWhenBuildTransactionStatusThenReturnPendingAndAmountSubstractingFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now().plus(1, ChronoUnit.DAYS))
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, ATM);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.PENDING)
                .amount(AMOUNT.subtract(FEE))
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

    @Test
    public void givenInternalChannelAndTransactionGreaterThanTodayWhenBuildTransactionStatusThenReturnFutureAndAmountAndFee() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now().plus(1, ChronoUnit.DAYS))
                .amount(AMOUNT)
                .fee(FEE)
                .description("Restaurant payment")
                .build();
        TransactionStatusHelper transactionStatusHelper = new TransactionStatusHelper();

        // WHEN
        TransactionStatus transactionStatus = transactionStatusHelper.buildTransactionStatus(transaction, INTERNAL);

        // THEN
        TransactionStatus expected = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.FUTURE)
                .amount(AMOUNT)
                .fee(FEE)
                .build();
        assertThat(transactionStatus).isEqualTo(expected);
    }

}
