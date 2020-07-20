package com.sergiomartinrubio.transactionsservice.controller;


import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatusParams;
import com.sergiomartinrubio.transactionsservice.service.TransactionStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status")
public class TransactionStatusController {

    private final TransactionStatusService transactionStatusService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public TransactionStatus getTransactionStatus(@RequestBody TransactionStatusParams transactionStatusParams) {
        return transactionStatusService.getTransactionStatus(transactionStatusParams.getReference(), transactionStatusParams.getChannel());
    }
}
