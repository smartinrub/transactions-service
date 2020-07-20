package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account findById(String accountIban);

    void updateBalance(BigDecimal balance, String accountIban);
}
