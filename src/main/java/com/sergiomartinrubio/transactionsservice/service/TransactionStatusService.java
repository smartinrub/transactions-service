package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;

import java.util.UUID;

public interface TransactionStatusService {
    TransactionStatus getTransactionStatus(UUID reference, Channel channel);
}
