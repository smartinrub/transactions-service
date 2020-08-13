package com.sergiomartinrubio.transactionsservice.bdd.stepdefs;

import com.sergiomartinrubio.transactionsservice.bdd.CucumberSpringContextConfiguration;
import com.sergiomartinrubio.transactionsservice.model.*;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class TransactionStatusStepDefsTest extends CucumberSpringContextConfiguration {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE = UUID.randomUUID();
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final String DESCRIPTION = "Restaurant payment";
    private static final ZonedDateTime DATE_BEFORE_NOW = ZonedDateTime.now().minus(1, DAYS);
    private static final ZonedDateTime DATE_NOW = ZonedDateTime.now();
    private static final ZonedDateTime DATE_AFTER_NOW = ZonedDateTime.now().plus(1, DAYS);

    private final TransactionStatusParams params = new TransactionStatusParams();

    private ResponseEntity<TransactionStatus> response;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterType("CLIENT|ATM|INTERNAL")
    public Channel channel(String channel) {
        return Channel.valueOf(channel);
    }

    @ParameterType("PENDING|SETTLED|FUTURE|INVALID")
    public Status status(String channel) {
        return Status.valueOf(channel);
    }

    @After
    public void setup() {
        transactionRepository.deleteAll();
    }

    @Given("A transaction that is not stored in our system")
    public void a_transaction_that_is_not_stored_in_our_system() {

    }

    @Given("A transaction that is stored in our system with date before today")
    public void a_transaction_that_is_stored_in_our_system_with_date_before_today() {
        Transaction transactionBeforeToday = createTransaction(DATE_BEFORE_NOW);
        transactionService.save(transactionBeforeToday);
    }

    @Given("A transaction that is stored in our system with date today")
    public void a_transaction_that_is_stored_in_our_system_with_date_today() {
        Transaction transactionToday = createTransaction(DATE_NOW);
        transactionService.save(transactionToday);
    }

    @Given("A transaction that is stored in our system with date after today")
    public void a_transaction_that_is_stored_in_our_system_with_date_after_today() {
        Transaction transactionAfterToday = createTransaction(DATE_AFTER_NOW);
        transactionService.save(transactionAfterToday);
    }

    @When("I check the status from any channel")
    public void i_check_the_status_from_any_channel() {
        params.setReference(UUID.randomUUID());
        params.setChannel(Channel.CLIENT);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<TransactionStatusParams> statusRequest = new HttpEntity<>(params, httpHeaders);
        response = restTemplate.exchange("/status", HttpMethod.POST, statusRequest, TransactionStatus.class);
    }

    @When("I check the status from {channel} channel")
    public void i_check_the_status_from_a_given_channel(Channel channel) {
        params.setChannel(channel);
        params.setReference(TRANSACTION_REFERENCE);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<TransactionStatusParams> statusRequest = new HttpEntity<>(params, httpHeaders);
        response = restTemplate.exchange("/status", HttpMethod.POST, statusRequest, TransactionStatus.class);
    }

    @Then("The system returns the status {status}")
    public void the_system_returns_the_status(Status status) {
        assertThat(response.getBody().getStatus()).isEqualTo(status);
    }

    @And("And the amount substracting the fee")
    public void and_the_amount_substracting_the_fee() {
        assertThat(response.getBody().getAmount()).isEqualTo(AMOUNT.subtract(FEE));
    }

    @And("And the amount")
    public void and_the_amount() {
        assertThat(response.getBody().getAmount()).isEqualTo(AMOUNT);
    }

    @And("And the fee")
    public void and_the_fee() {
        assertThat(response.getBody().getFee()).isEqualTo(FEE);
    }

    private Transaction createTransaction(ZonedDateTime date) {
        return Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .date(date)
                .amount(AMOUNT)
                .fee(FEE)
                .description(DESCRIPTION)
                .build();
    }

}
