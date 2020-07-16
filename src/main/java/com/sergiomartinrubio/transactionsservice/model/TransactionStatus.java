package com.sergiomartinrubio.transactionsservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionStatus {
    private final String reference;
    private final Status status;
    private final BigDecimal amount;
    private final BigDecimal fee;
}
