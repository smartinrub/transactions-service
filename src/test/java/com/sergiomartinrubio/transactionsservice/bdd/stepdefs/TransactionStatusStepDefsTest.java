package com.sergiomartinrubio.transactionsservice.bdd.stepdefs;

import com.sergiomartinrubio.transactionsservice.bdd.CucumberSpringContextConfiguration;
import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatusParams;
import com.sergiomartinrubio.transactionsservice.repository.TransactionRepository;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;


public class TransactionStatusStepDefsTest extends CucumberSpringContextConfiguration {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE_BEFORE_TODAY = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final UUID TRANSACTION_REFERENCE_TODAY = UUID.fromString("39e819bc-f863-4b9b-8f3f-d6c107c21142");
    private static final UUID TRANSACTION_REFERENCE_AFTER_TODAY = UUID.fromString("cbaad0a9-e94c-4e77-adce-bd797dd70f62");
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

    @Before
    public void setup() {
        transactionRepository.deleteAll();
    }

    @Given("A transaction that is not stored in our system")
    public void a_transaction_that_is_not_stored_in_our_system() {

    }

    @Given("A transaction that is stored in our system with date before today")
    public void a_transaction_that_is_stored_in_our_system_with_date_before_today() {
        Transaction transactionBeforeToday = Transaction.builder()
                .reference(TRANSACTION_REFERENCE_BEFORE_TODAY)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE_BEFORE_NOW)
                .amount(AMOUNT)
                .fee(FEE)
                .description(DESCRIPTION)
                .build();
        transactionService.save(transactionBeforeToday);
    }

    @Given("A transaction that is stored in our system with date today")
    public void a_transaction_that_is_stored_in_our_system_with_date_today() {
        Transaction transactionToday = Transaction.builder()
                .reference(TRANSACTION_REFERENCE_TODAY)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE_NOW)
                .amount(AMOUNT)
                .fee(FEE)
                .description(DESCRIPTION)
                .build();
        transactionService.save(transactionToday);
    }

    @Given("A transaction that is stored in our system with date after today")
    public void a_transaction_that_is_stored_in_our_system_with_date_after_today() {
        Transaction transactionAfterToday = Transaction.builder()
                .reference(TRANSACTION_REFERENCE_AFTER_TODAY)
                .accountIban(ACCOUNT_IBAN)
                .date(DATE_AFTER_NOW)
                .amount(AMOUNT)
                .fee(FEE)
                .description(DESCRIPTION)
                .build();
        transactionService.save(transactionAfterToday);
    }

    @When("I check the status from any channel")
    public void i_check_the_status_from_any_channel() {
        params.setReference(UUID.randomUUID());
        params.setChannel(Channel.CLIENT);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<TransactionStatusParams> statusRequest = new HttpEntity<>(params, httpHeaders);
        response = restTemplate.exchange("/status", HttpMethod.POST, statusRequest, TransactionStatus.class);
    }

    @When("I check the status from {string} or {string} channel")
    public void i_check_the_status_from_client_or_atm_channel(String clientChannel, String atmChannel) {
        params.setChannel(Channel.valueOf(clientChannel));
    }

    @When("I check the status from {string} channel")
    public void i_check_the_status_from_internal_channel(String clientChannel) {
        params.setChannel(Channel.valueOf(clientChannel));
    }

    @And("And the transaction date is before today")
    public void and_the_transaction_date_is_before_today() {
        params.setReference(TRANSACTION_REFERENCE_BEFORE_TODAY);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<TransactionStatusParams> statusRequest = new HttpEntity<>(params, httpHeaders);
        response = restTemplate.exchange("/status", HttpMethod.POST, statusRequest, TransactionStatus.class);
    }

    @And("And the transaction date is equals to today")
    public void and_the_transaction_date_is_equals_to_today() {
        params.setReference(TRANSACTION_REFERENCE_TODAY);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<TransactionStatusParams> statusRequest = new HttpEntity<>(params, httpHeaders);
        response = restTemplate.exchange("/status", HttpMethod.POST, statusRequest, TransactionStatus.class);

    }

    @And("And the transaction date is greater than today")
    public void and_the_transaction_date_is_greater_than_today() {
        params.setReference(TRANSACTION_REFERENCE_AFTER_TODAY);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<TransactionStatusParams> statusRequest = new HttpEntity<>(params, httpHeaders);
        response = restTemplate.exchange("/status", HttpMethod.POST, statusRequest, TransactionStatus.class);

    }

    @Then("The system returns the status {string}")
    public void the_system_returns_the_status(String transactionStatus) {
        assertThat(response.getBody().getStatus().toString()).isEqualTo(transactionStatus);
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

}
