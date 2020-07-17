package com.sergiomartinrubio.transactionsservice.repository;

import com.sergiomartinrubio.transactionsservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Modifying
    @Query("UPDATE Account a SET a.balance = ?1 WHERE account_iban = ?2")
    void updateBalance(BigDecimal balance, String accountIban);
}
