package com.sergiomartinrubio.transactionsservice.controller;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatusParams;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public List<Transaction> searchTransaction(@PathVariable("accountIban") String accountIban,
                                               @RequestParam(value = "orderType", required = false) OrderType orderType) {
        return transactionService.searchTransaction(accountIban, orderType);
    }

    @PostMapping(value = "/transactions/status", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public TransactionStatus getTransactionStatus(@RequestBody TransactionStatusParams transactionStatusParams) {
        return transactionService.getTransactionStatus(transactionStatusParams.getReference(), transactionStatusParams.getChannel());
    }
}
