package com.sergiomartinrubio.transactionsservice.service.impl;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountBalanceCalculatorImplTest {

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

    private final AccountBalanceCalculatorImpl accountBalanceCalculator = new AccountBalanceCalculatorImpl();

    @Test
    void givenTransactionAndAccountWhenCalculateThenReturnBalance() {
        // GIVEN
        Account account = new Account(ACCOUNT_IBAN, new BigDecimal(100));

        // WHEN
        BigDecimal balance = accountBalanceCalculator.calculate(TRANSACTION, account);

        // THEN
        assertThat(balance).isEqualTo(new BigDecimal("290.20"));
    }

}