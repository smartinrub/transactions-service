package com.sergiomartinrubio.transactionsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.UUID;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidTransactionException extends RuntimeException {

    private static final String MESSAGE = "Transaction is not allowed, balance will be %s for transaction %s";

    public InvalidTransactionException(BigDecimal balance, UUID reference) {
        super(String.format(MESSAGE, balance.toString(), reference.toString()));
    }
}
