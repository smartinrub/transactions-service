package com.sergiomartinrubio.transactionsservice.bdd;

import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringContextConfiguration {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        transactionRepository.deleteAll();
    }
}
