package com.sergiomartinrubio.transactionsservice.util;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static com.sergiomartinrubio.transactionsservice.model.Status.*;

@Component
public class TransactionStatusTransformer {

    public TransactionStatus transform(Transaction transaction, Channel channel) {
        ZonedDateTime transactionDate = transaction.getDate();
        TransactionStatus.TransactionStatusBuilder transactionStatusBuilder = TransactionStatus.builder()
                .reference(transaction.getReference());
        // Assuming when channel is not provided we default to CLIENT
        if (channel == null) {
            channel = Channel.CLIENT;
        }
        switch (channel) {
            case CLIENT:
            default:
                transactionStatusBuilder.amount(getAmount(transaction));
                if (isTransactionBeforeToday(transactionDate)) {
                    transactionStatusBuilder.status(SETTLED);
                } else if (isTransactionToday(transactionDate)) {
                    transactionStatusBuilder.status(PENDING);
                } else {
                    transactionStatusBuilder.status(FUTURE);
                }
                return transactionStatusBuilder.build();
            case ATM:
                transactionStatusBuilder.amount(getAmount(transaction));
                if (isTransactionBeforeToday(transactionDate)) {
                    transactionStatusBuilder.status(SETTLED);
                } else {
                    transactionStatusBuilder.status(PENDING);
                }
                return transactionStatusBuilder.build();
            case INTERNAL:
                transactionStatusBuilder
                        .amount(transaction.getAmount())
                        .fee(transaction.getFee());
                if (isTransactionBeforeToday(transactionDate)) {
                    transactionStatusBuilder.status(SETTLED);
                } else if (isTransactionToday(transactionDate)) {
                    transactionStatusBuilder.status(PENDING);
                } else {
                    transactionStatusBuilder.status(FUTURE);
                }
                return transactionStatusBuilder.build();
        }
    }

    private boolean isTransactionBeforeToday(ZonedDateTime date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return LocalDate.of(year, month, day).isBefore(LocalDate.now());
    }

    private boolean isTransactionToday(ZonedDateTime date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return LocalDate.of(year, month, day).isEqual(LocalDate.now());
    }

    private BigDecimal getAmount(Transaction transaction) {
        return transaction.getFee() != null
                ? transaction.getAmount().subtract(transaction.getFee())
                : transaction.getAmount();
    }
}
