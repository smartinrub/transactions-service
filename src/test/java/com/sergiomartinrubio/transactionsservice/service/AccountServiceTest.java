package com.sergiomartinrubio.transactionsservice.service;

import com.sergiomartinrubio.transactionsservice.exception.AccountNotFoundException;
import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    void givenAccountIbanWhenFindByIdThenReturnAccount() {
        // GIVEN
        Account savedAccount = new Account(ACCOUNT_IBAN, new BigDecimal(100));
        when(accountRepository.findById(ACCOUNT_IBAN)).thenReturn(Optional.of(savedAccount));

        // WHEN
        Account account = accountService.findById(ACCOUNT_IBAN);

        // THEN
        assertThat(account).isEqualTo(savedAccount);
    }

    @Test
    void givenAccountIbanNotFoundWhenFindByIdThenThrowAccountNotFoundException() {
        // GIVEN
        when(accountRepository.findById(ACCOUNT_IBAN)).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThatThrownBy(() -> accountService.findById(ACCOUNT_IBAN))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void givenBalanceAndAccountIbanWhenUpdateBalanceThenUpdateIsCalled() {
        // GIVEN
        BigDecimal newBalance = new BigDecimal(100);

        // WHEN
        accountService.updateBalance(newBalance, ACCOUNT_IBAN);

        // THEN
        verify(accountRepository).updateBalance(newBalance, ACCOUNT_IBAN);
    }
}