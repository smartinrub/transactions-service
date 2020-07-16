package com.sergiomartinrubio.transactionsservice.controller;

import com.sergiomartinrubio.transactionsservice.model.*;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transactions")
    public void createTransaction(@RequestBody @Valid Transaction transaction) {
        transactionService.createTransaction(transaction);
    }

    @GetMapping("/transactions/ibans/{accountIban}")
    public Transaction searchTransaction(@PathVariable("accountIban") String accountIban) {
        return transactionService.searchTransaction(accountIban);
    }

    // We shouldn't use a request body for GET operations
    // in order to follow REST principles, so instead we can use path parameters
    // with a request parameter. If security is a concern we can use POST instead, but again
    // we will not following REST principles, because POST is not for idempotent requests
    @GetMapping("/transactions/{reference}/status")
    public TransactionStatus getTransactionStatus(@PathVariable("reference") String reference,
                                                  @RequestParam("channel") Channel channel) {
        return new TransactionStatus("XXXXXX", Status.INVALID);
    }
}
