package com.sergiomartinrubio.transactionsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusParams {

    @NotNull
    private UUID reference;
    private Channel channel;
}
