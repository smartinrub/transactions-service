package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.util.TransactionStatusTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionStatusService {

    private final TransactionRepository transactionRepository;
    private final TransactionStatusTransformer transactionStatusTransformer;

    public TransactionStatus getTransactionStatus(UUID reference, Channel channel) {
        Optional<Transaction> transaction = transactionRepository.findById(reference);
        if (transaction.isEmpty()) {
            return TransactionStatus.builder()
                    .reference(reference)
                    .status(Status.INVALID)
                    .build();
        }

        return transactionStatusTransformer.transform(transaction.get(), channel);
    }

}
