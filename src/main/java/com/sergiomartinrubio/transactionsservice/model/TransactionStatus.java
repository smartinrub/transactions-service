package com.sergiomartinrubio.transactionsservice.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class TransactionStatus {
    UUID reference;
    Status status;
    BigDecimal amount;
    BigDecimal fee;
}
