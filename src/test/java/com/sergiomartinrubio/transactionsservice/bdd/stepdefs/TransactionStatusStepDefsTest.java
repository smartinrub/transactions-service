package com.sergiomartinrubio.transactionsservice.bdd.stepdefs;

import com.sergiomartinrubio.transactionsservice.bdd.CucumberSpringContextConfiguration;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


public class TransactionStatusStepDefsTest extends CucumberSpringContextConfiguration {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42.000Z");
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final String DESCRIPTION = "Restaurant payment";

    private ResponseEntity<String> response;

    @Autowired
    private TestRestTemplate restTemplate;

    @Given("A transaction that is stored in our system")
    public void a_transaction_that_is_stored_in_our_system() {
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
        response = restTemplate
                .exchange("/transactions", HttpMethod.POST, request, String.class);
    }

    @Given("A transaction that is not stored in our system")
    public void a_transaction_that_is_not_stored_in_our_system() {

    }

    @When("I check the status from any channel")
    public void i_check_the_status_from_any_channel() {

    }

    @When("I check the status from {string} or {string} channel")
    public void i_check_the_status_from_client_or_atm_channel(String clientChannel, String atmChannel) {

    }

    @When("I check the status from {string} channel")
    public void i_check_the_status_from_internal_channel(String clientChannel) {

    }

    @And("And the transaction date is before today")
    public void and_the_transaction_date_is_before_today() {

    }

    @And("And the transaction date is equals to today")
    public void and_the_transaction_date_is_equals_to_today() {

    }

    @And("And the transaction date is greater than today")
    public void and_the_transaction_date_is_greater_than_today() {

    }

    @Then("The system returns the status {string}")
    public void the_system_returns_the_status_settled(String transactionStatus) {

    }

    @And("And the amount substracting the fee")
    public void and_the_amount_substracting_the_fee() {

    }

    @And("And the amount")
    public void and_the_amount() {

    }

    @And("And the fee")
    public void and_the_fee() {

    }
}
