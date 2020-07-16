package com.sergiomartinrubio.transactionsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Transaction not found for account iban %s";
    private static final String MESSAGE_REFERENCE = "Transaction not found for reference %s";

    public TransactionNotFoundException(String accountIban) {
        super(String.format(MESSAGE, accountIban));
    }

    public TransactionNotFoundException(UUID reference) {
        super(String.format(MESSAGE_REFERENCE, reference));
    }
}
