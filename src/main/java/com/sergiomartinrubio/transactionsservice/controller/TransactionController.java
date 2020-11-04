package com.sergiomartinrubio.transactionsservice.controller;

import com.sergiomartinrubio.transactionsservice.model.OrderType;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.service.TransactionProcessorService;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionProcessorService transactionProcessorService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createTransaction(@RequestBody @Valid Transaction transaction) {
        transactionProcessorService.process(transaction);
    }

    @GetMapping
    public List<Transaction> searchTransactions(@RequestParam("accountIban") String accountIban,
                                                @RequestParam(value = "orderType", required = false) OrderType orderType) {
        return transactionService.findByIban(accountIban, orderType);
    }

}
