package com.sergiomartinrubio.transactionsservice.repository;

import com.sergiomartinrubio.transactionsservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
