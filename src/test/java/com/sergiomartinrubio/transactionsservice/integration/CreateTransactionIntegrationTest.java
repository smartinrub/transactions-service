package com.sergiomartinrubio.transactionsservice.integration;

import com.sergiomartinrubio.transactionsservice.model.Account;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateTransactionIntegrationTest {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42.000Z");
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final String DESCRIPTION = "Restaurant payment";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        accountRepository.save(new Account(ACCOUNT_IBAN, BigDecimal.valueOf(200)));
    }

    @Test
    void givenTransactionWhenPostTransactionsThenReturnCreatedResponseStatus() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE)
                .amount(AMOUNT)
                .fee(FEE)
                .description(DESCRIPTION)
                .build();
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        // WHEN
        ResponseEntity<String> response = restTemplate
                .exchange("/transactions", HttpMethod.POST, request, String.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void givenMissingMandatoryAttributeWhenPostTransactionsThenReturnBadRequestStatus() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .date(DATE)
                .amount(AMOUNT)
                .fee(FEE)
                .description(DESCRIPTION)
                .build();
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        // WHEN
        ResponseEntity<String> response = restTemplate
                .exchange("/transactions", HttpMethod.POST, request, String.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}