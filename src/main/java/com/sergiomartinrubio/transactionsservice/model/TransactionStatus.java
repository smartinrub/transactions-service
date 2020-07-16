package com.sergiomartinrubio.transactionsservice.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class TransactionStatus {
    private final UUID reference;
    private final Status status;
    private final BigDecimal amount;
    private final BigDecimal fee;
}
