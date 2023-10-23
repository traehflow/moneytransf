package com.vsoft.moneytransf.test.integration;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.dto.InputTransactionDTO;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.dto.OutputTransactionDTO;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TransactionsIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;


    /**
     * 1. Test login and several transactions executions
     *  Authorize -> Reversal -> Charge -> Refund
     * 2. Tested cases of invalid inpud
     * 3. Total transaction sum is checked.
     */
    @Test
    public void completePathTest() {
        String username = "johnwill@merchant.com";
        String password = "password";

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/login?username={username}&password={password}", null, String.class, username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());


        String cookies = response.getHeaders().get("Set-Cookie").get(0);
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setEmail("johnwill@merchant.com");
        merchantDTO.setName("name");
        merchantDTO.setDescription("description");
        merchantDTO.setStatus(MerchantStatus.ENABLED);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.put("Cookie", List.of( cookies));


        ResponseEntity<List<Merchant>> merchantsResponse = restTemplate.exchange(
                "/merchants/", HttpMethod.GET, new HttpEntity(merchantDTO, headers), new ParameterizedTypeReference<List<Merchant>>() {
                });


        Assertions.assertEquals(merchantsResponse.getBody().size(),1);

        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.AUTHORIZE);
        inputTransactionDTO.setAmount(new BigDecimal(100));
        OutputTransactionDTO outputTransactionDTO;
        testTransaction(inputTransactionDTO, headers, HttpStatus.BAD_REQUEST);

        inputTransactionDTO.setCustomerPhone("880880880");
        inputTransactionDTO.setCustomerEmail("invalidEmail");
        testTransaction(inputTransactionDTO, headers, HttpStatus.BAD_REQUEST);

        inputTransactionDTO.setCustomerEmail("validEmail@merchant.com");
        outputTransactionDTO = testTransaction(inputTransactionDTO, headers, HttpStatus.OK);

        UUID transactionId = outputTransactionDTO.getTransactionId();

        inputTransactionDTO.setTransactionType(TransactionDescriminator.REVERSAL);
        inputTransactionDTO.setReferencedTransactionId(UUID.randomUUID());

        testTransaction(inputTransactionDTO, headers, HttpStatus.BAD_REQUEST);

        inputTransactionDTO.setReferencedTransactionId(transactionId);
        testTransaction(inputTransactionDTO, headers, HttpStatus.OK);

        inputTransactionDTO.setTransactionType(TransactionDescriminator.CHARGE);
        inputTransactionDTO.setAmount(new BigDecimal(3000));
        inputTransactionDTO.setReferencedTransactionId(null);
        outputTransactionDTO = testTransaction(inputTransactionDTO, headers, HttpStatus.OK);

        UUID chargeTransactionId = outputTransactionDTO.getTransactionId();

        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        inputTransactionDTO.setReferencedTransactionId(UUID.randomUUID());
        testTransaction(inputTransactionDTO, headers, HttpStatus.BAD_REQUEST);

        inputTransactionDTO.setReferencedTransactionId(chargeTransactionId);
        inputTransactionDTO.setAmount(new BigDecimal(1500));
        testTransaction(inputTransactionDTO, headers, HttpStatus.OK);

        inputTransactionDTO.setReferencedTransactionId(chargeTransactionId);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        testTransaction(inputTransactionDTO, headers, HttpStatus.OK);

        inputTransactionDTO.setReferencedTransactionId(chargeTransactionId);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        testTransaction(inputTransactionDTO, headers, HttpStatus.BAD_REQUEST);

        ResponseEntity<List<Merchant>> merchants  = restTemplate.exchange(
                "/merchants/", HttpMethod.GET, new HttpEntity(inputTransactionDTO, headers),  new ParameterizedTypeReference<List<Merchant>>() {});


        Assertions.assertEquals(merchants.getBody().get(0).getTotalTransactionSum(), new BigDecimal("500.00"));
    }

    private OutputTransactionDTO testTransaction(InputTransactionDTO inputTransactionDTO, MultiValueMap<String, String> headers, HttpStatus ok) {
        ResponseEntity<OutputTransactionDTO> result;
        result = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers), OutputTransactionDTO.class);
        assertEquals(ok, result.getStatusCode());
        return result.getBody();
    }

}