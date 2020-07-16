package com.sergiomartinrubio.transactionsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Transaction not found for account iban %s";

    public TransactionNotFoundException(String accountIban) {
        super(String.format(MESSAGE, accountIban));
    }
}
