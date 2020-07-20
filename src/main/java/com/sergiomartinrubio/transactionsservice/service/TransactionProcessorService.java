package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Transaction;

public interface TransactionProcessorService {

    void process(Transaction transaction);
}
