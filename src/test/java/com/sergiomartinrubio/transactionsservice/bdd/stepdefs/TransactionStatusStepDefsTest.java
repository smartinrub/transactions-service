package com.sergiomartinrubio.transactionsservice.bdd.stepdefs;

import com.sergiomartinrubio.transactionsservice.bdd.CucumberSpringContextConfiguration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class TransactionStatusStepDefsTest extends CucumberSpringContextConfiguration {

    @Given("A transaction that is stored in our system")
    public void a_transaction_that_is_stored_in_our_system() {

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
