package com.sergiomartinrubio.transactionsservice.exception;

public class TransactionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Transaction not found for account iban %s";

    public TransactionNotFoundException(String accountIban) {
        super(String.format(MESSAGE, accountIban));
    }
}
