package com.vsoft.moneytransf.test;

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



        ResponseEntity<Void> merchantResponse = restTemplate.exchange(
                "/merchants/", HttpMethod.PUT, new HttpEntity(merchantDTO, headers), Void.class);

        ResponseEntity<List<Merchant>> merchantsResponse = restTemplate.exchange(
                "/merchants/", HttpMethod.GET, new HttpEntity(merchantDTO, headers), new ParameterizedTypeReference<List<Merchant>>() {
                });


        Assertions.assertEquals(merchantsResponse.getBody().size(),1);

        InputTransactionDTO inputTransactionDTO = new InputTransactionDTO();
        inputTransactionDTO.setTransactionType(TransactionDescriminator.AUTHORIZE);
        inputTransactionDTO.setAmount(new BigDecimal(100));
        ResponseEntity<OutputTransactionDTO> outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, outputTransaction.getStatusCode());

        inputTransactionDTO.setCustomerPhone("880880880");
        inputTransactionDTO.setCustomerEmail("invalidEmail");
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, outputTransaction.getStatusCode());

        inputTransactionDTO.setCustomerEmail("validEmail@merchant.com");
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.OK, outputTransaction.getStatusCode());

        UUID transactionId = outputTransaction.getBody().getTransactionId();

        inputTransactionDTO.setTransactionType(TransactionDescriminator.REVERSAL);
        inputTransactionDTO.setReferencedTransactionId(UUID.randomUUID());

        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, outputTransaction.getStatusCode());

        inputTransactionDTO.setReferencedTransactionId(transactionId);
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.OK, outputTransaction.getStatusCode());

        inputTransactionDTO.setTransactionType(TransactionDescriminator.CHARGE);
        inputTransactionDTO.setAmount(new BigDecimal(3000));
        inputTransactionDTO.setReferencedTransactionId(null);
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.OK, outputTransaction.getStatusCode());

        UUID chargeTransactionId = outputTransaction.getBody().getTransactionId();

        inputTransactionDTO.setTransactionType(TransactionDescriminator.REFUND);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        inputTransactionDTO.setReferencedTransactionId(UUID.randomUUID());
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, outputTransaction.getStatusCode());

        inputTransactionDTO.setReferencedTransactionId(chargeTransactionId);
        inputTransactionDTO.setAmount(new BigDecimal(1500));
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.OK, outputTransaction.getStatusCode());

        inputTransactionDTO.setReferencedTransactionId(chargeTransactionId);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.OK, outputTransaction.getStatusCode());

        inputTransactionDTO.setReferencedTransactionId(chargeTransactionId);
        inputTransactionDTO.setAmount(new BigDecimal(1000));
        outputTransaction = restTemplate.exchange(
                "/transactions/", HttpMethod.POST, new HttpEntity(inputTransactionDTO, headers),  OutputTransactionDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, outputTransaction.getStatusCode());

        ResponseEntity<List<Merchant>> merchants  = restTemplate.exchange(
                "/merchants/", HttpMethod.GET, new HttpEntity(inputTransactionDTO, headers),  new ParameterizedTypeReference<List<Merchant>>() {});


        Assertions.assertEquals(merchants.getBody().get(0).getTotalTransactionSum(), new BigDecimal("500.00"));
    }

    @Test
    public void testLogin2() {

    }
}