package com.sergiomartinrubio.transactionsservice.repository;

import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE account_iban = ?1")
    Optional<Transaction> searchTransaction(String accountIban);
}
