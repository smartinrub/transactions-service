package com.sergiomartinrubio.transactionsservice.controller;

import com.sergiomartinrubio.transactionsservice.model.Channel;
import com.sergiomartinrubio.transactionsservice.model.Status;
import com.sergiomartinrubio.transactionsservice.model.TransactionStatus;
import com.sergiomartinrubio.transactionsservice.service.TransactionStatusService;
import com.sergiomartinrubio.transactionsservice.service.impl.TransactionStatusServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionStatusController.class)
class TransactionStatusControllerTest {

    private static final UUID TRANSACTION_REFERENCE = UUID.fromString("f8145c28-4730-4afc-8cf5-11934d94b06f");
    private static final BigDecimal AMOUNT = new BigDecimal("193.38");
    private static final BigDecimal FEE = new BigDecimal("3.18");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionStatusService transactionStatusService;

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
        when(transactionStatusService.getTransactionStatus(TRANSACTION_REFERENCE, Channel.CLIENT)).thenReturn(transactionStatus);

        // WHEN
        MvcResult result = mockMvc
                .perform(post("/status")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }

}