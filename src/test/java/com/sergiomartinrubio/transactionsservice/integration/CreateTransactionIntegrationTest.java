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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateTransactionIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void givenTransactionWhenPostTransactionsThenReturnCreatedResponseStatus() {
        // GIVEN
        Transaction transaction = Transaction.builder()
                .reference("12345A")
                .accountIban("ES9820385778983000760236")
                .date("2019-07-16T16:55:42.000Z")
                .amount(new BigDecimal("193.38"))
                .fee(new BigDecimal("3.18"))
                .description("Restaurant payment")
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
                .reference("12345A")
                .date("2019-07-16T16:55:42.000Z")
                .amount(new BigDecimal("193.38"))
                .fee(new BigDecimal("3.18"))
                .description("Restaurant payment")
                .build();
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        // WHEN
        ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:" + port + "/transactions", HttpMethod.POST, request, String.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}