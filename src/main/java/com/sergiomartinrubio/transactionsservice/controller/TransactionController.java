package com.sergiomartinrubio.transactionsservice.controller;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createTransaction(@RequestBody @Valid Transaction transaction) {
        transactionService.createTransaction(transaction);
    }

    @GetMapping("/{accountIban}")
    public List<Transaction> searchTransactions(@PathVariable("accountIban") String accountIban,
                                                @RequestParam(value = "orderType", required = false) OrderType orderType) {
        return transactionService.searchTransaction(accountIban, orderType);
    }

}
