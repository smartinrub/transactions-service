package com.sergiomartinrubio.transactionsservice.model;

import lombok.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Value
@Entity
public class Account {

    @Id
    @Column(name = "account_iban")
    private final String accountIban;

    private final BigDecimal balance;
}
