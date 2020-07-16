package com.sergiomartinrubio.transactionsservice.integration;

import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.UUID;

import static com.sergiomartinrubio.transactionsservice.model.Channel.CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetTransactionStatusIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenGetTransactionsStatusThenReturnTransactionStatusAndOkayResponse() {
        // WHEN
        ResponseEntity<TransactionStatus> response = restTemplate
                .getForEntity("http://localhost:" + port +"/transactions/{reference}/status?channel={channel}",
                        TransactionStatus.class, UUID.randomUUID(), CLIENT);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}