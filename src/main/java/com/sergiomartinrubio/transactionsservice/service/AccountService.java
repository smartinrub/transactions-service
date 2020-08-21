package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.AccountNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account findById(String accountIban) {
        return accountRepository.findById(accountIban)
                .orElseThrow(() -> new AccountNotFoundException(accountIban));
    }

    public void updateBalance(BigDecimal balance, String accountIban) {
        accountRepository.updateBalance(balance, accountIban);
    }
}
