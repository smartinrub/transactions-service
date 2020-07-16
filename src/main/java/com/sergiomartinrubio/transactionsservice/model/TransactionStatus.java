package com.sergiomartinrubio.transactionsservice.model;

import lombok.Data;

@Data
public class TransactionStatus {

    private final String reference;

    private final Status status;

}
