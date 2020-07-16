package com.sergiomartinrubio.transactionsservice.model;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
@Value
public class Transaction {

    @Length(min = 6, max = 6)
    private final String reference;

    @NotBlank
    private final String accountIban;

    private final ZonedDateTime date;

    @NotNull
    private final BigDecimal amount;

    @DecimalMin(value = "0.0", inclusive = false)
    private final BigDecimal fee;

    private final String description;
}
