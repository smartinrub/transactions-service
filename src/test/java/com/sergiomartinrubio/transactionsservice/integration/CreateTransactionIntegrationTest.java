package com.sergiomartinrubio.transactionsservice.integration;

import com.sergiomartinrubio.transactionsservice.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateTransactionIntegrationTest {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42.000Z");
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final String DESCRIPTION = "Restaurant payment";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void givenTransactionWhenPostTransactionsThenReturnCreatedResponseStatus() {
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
                .exchange("http://localhost:" + port + "/transactions", HttpMethod.POST, request, String.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void givenMissingMandatoryAttributeWhenPostTransactionsThenReturnBadRequestStatus() {
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
                .exchange("http://localhost:" + port + "/transactions", HttpMethod.POST, request, String.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}