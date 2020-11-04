package com.sergiomartinrubio.transactionsservice.integration;

import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchTransactionIntegrationTest {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T15:55:42Z[UTC]");
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final String DESCRIPTION = "Restaurant payment";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        transactionRepository.deleteAll();
    }

    @Test
    void whenSearchTransactionsThenReturnCreatedResponseStatus() {
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
        restTemplate.exchange("/transactions", HttpMethod.POST, request, String.class);

        // WHEN
        ResponseEntity<String> response = restTemplate
                .getForEntity("/transactions?accountIban={iban}", String.class, ACCOUNT_IBAN);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[{\"reference\":\"f8145c28-4730-4afc-8cf5-11934d94b06f\"," +
                "\"accountIban\":\"ES9820385778983000760236\"," +
                "\"date\":\"2019-07-16T17:55:42+02:00\"," +
                "\"amount\":193.38," +
                "\"fee\":3.18," +
                "\"description\":\"Restaurant payment\"}]");
    }

}