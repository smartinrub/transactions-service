package com.sergiomartinrubio.transactionsservice.service.impl;

import com.sergiomartinrubio.transactionsservice.exception.AccountNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import com.sergiomartinrubio.transactionsservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account findById(String accountIban) {
        return accountRepository.findById(accountIban)
                .orElseThrow(() -> new AccountNotFoundException(accountIban));
    }

    @Override
    public void updateBalance(BigDecimal balance, String accountIban) {
        accountRepository.updateBalance(balance, accountIban);
    }
}
