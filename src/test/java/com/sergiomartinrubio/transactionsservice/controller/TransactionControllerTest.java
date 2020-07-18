package com.sergiomartinrubio.transactionsservice.controller;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.Transaction;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    private static final String ACCOUNT_IBAN = "ES9820385778983000760236";
    private static final UUID TRANSACTION_REFERENCE = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final ZonedDateTime DATE = ZonedDateTime.parse("2019-07-16T16:55:42Z")
            .withZoneSameLocal(ZoneId.of("UTC"));
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");
    private static final String DESCRIPTION = "Restaurant payment";
    private static final Transaction TRANSACTION = Transaction.builder()
            .reference(TRANSACTION_REFERENCE)
            .accountIban(ACCOUNT_IBAN)
            .date(DATE)
            .amount(AMOUNT)
            .fee(FEE)
            .description(DESCRIPTION)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void givenTransactionWhenPostRequestToTransactionsThenCreateTransactionIsCalledAndReturnCreated() throws Exception {
        // GIVEN
        String requestBody = "{\"reference\":\"f8145c28-4730-4afc-8cf5-11934d94b06f\"," +
                "\"accountIban\":\"ES9820385778983000760236\"," +
                "\"date\":\"2019-07-16T16:55:42Z\"," +
                "\"amount\":193.38," +
                "\"fee\":3.18," +
                "\"description\":\"Restaurant payment\"}";

        // WHEN
        mockMvc.perform(post("/transactions")
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());

        // THEN
        verify(transactionService).createTransaction(TRANSACTION);
    }

    @Test
    public void givenAccountIbanWhenGetRequestToTransactionsThenReturnTransaction() throws Exception {
        // GIVEN
        String responseBody = "[{\"reference\":\"f8145c28-4730-4afc-8cf5-11934d94b06f\"," +
                "\"accountIban\":\"ES9820385778983000760236\"," +
                "\"date\":\"2019-07-16T16:55:42Z\"," +
                "\"amount\":193.38," +
                "\"fee\":3.18," +
                "\"description\":\"Restaurant payment\"}]";
        when(transactionService.searchTransaction(ACCOUNT_IBAN, null)).thenReturn(List.of(TRANSACTION));

        // WHEN
        MvcResult result = mockMvc.perform(get("/transactions/ibans/" + ACCOUNT_IBAN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }

    @Test
    public void givenReferenceAndChannelWhenGetTransactionStatusThenReturnTransactionStatus() throws Exception {
        // GIVEN
        String responseBody = "{" +
                "\"reference\":\"f8145c28-4730-4afc-8cf5-11934d94b06f\"," +
                "\"status\":\"PENDING\"," +
                "\"amount\":193.38," +
                "\"fee\":3.18" +
                "}";
        String requestBody = "{\"reference\":\"f8145c28-4730-4afc-8cf5-11934d94b06f\"," +
                "\"channel\":\"CLIENT\"}";
        TransactionStatus transactionStatus = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(Status.PENDING)
                .amount(AMOUNT)
                .fee(FEE)
                .build();
        when(transactionService.getTransactionStatus(TRANSACTION_REFERENCE, Channel.CLIENT)).thenReturn(transactionStatus);

        // WHEN
        MvcResult result = mockMvc
                .perform(post("/transactions/status")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }

}