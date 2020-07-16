package com.sergiomartinrubio.transactionsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    // assuming we want to use a proper unique identifier for the transactions
    @Id
    private UUID reference;

    @NotBlank
    @Column(name = "account_iban")
    private String accountIban;

    private ZonedDateTime date;

    @NotNull
    private BigDecimal amount;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal fee;

    private String description;
}
