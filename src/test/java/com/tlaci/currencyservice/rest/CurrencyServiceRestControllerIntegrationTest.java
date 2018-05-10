package com.tlaci.currencyservice.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tlaci.currencyservice.rest.dto.ConversionResultDTO;
import com.tlaci.currencyservice.rest.dto.ExchangeRateDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyServiceRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testConvert_success() {
        ResponseEntity<ConversionResultDTO> responseEntity =
                restTemplate.getForEntity("/currency-service/convert/from/{from}/to/{to}/type/{type}/quantity/{quantity}",
                        ConversionResultDTO.class, "USD", "HUF", "BUY", 100L);
        ConversionResultDTO result = responseEntity.getBody();

        assertEquals(HttpStatus.OK , responseEntity.getStatusCode());
        assertEquals("USD", result.getFrom());
        assertEquals("HUF", result.getTo());
        assertEquals("BUY", result.getType());
        assertEquals(BigDecimal.valueOf(100L), result.getQuantity());
        assertThat(BigDecimal.valueOf(26377.200), Matchers.comparesEqualTo(result.getAmount()));
    }

    @Test
    public void testConvert_unknownType() {
        ResponseEntity<Map> responseEntity =
                restTemplate.getForEntity("/currency-service/convert/from/{from}/to/{to}/type/{type}/quantity/{quantity}",
                        Map.class, "USD", "HUF", "WHAT", 100L);

        assertEquals(HttpStatus.BAD_REQUEST , responseEntity.getStatusCode());
    }

    @Test
    public void testConvert_unknownCurrency() {
        ResponseEntity<Map> responseEntity =
                restTemplate.getForEntity("/currency-service/convert/from/{from}/to/{to}/type/{type}/quantity/{quantity}",
                        Map.class, "MII", "HUF", "SELL", 100L);

        assertEquals(HttpStatus.NOT_FOUND , responseEntity.getStatusCode());
    }

    @Test
    public void testFindExchangeRates_success() {
        ResponseEntity<List> responseEntity =
                restTemplate.getForEntity("/currency-service/exchange-rates/from/{from}/type/{type}",
                        List.class, "USD", "SELL");
        List<ExchangeRateDTO> exchangeRates = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4, exchangeRates.size());
    }

    @Test
    public void testFindExchangeRates_unknownType() {
        ResponseEntity<Map> responseEntity =
                restTemplate.getForEntity("/currency-service/exchange-rates/from/{from}/type/{type}",
                        Map.class, "USD", "HMMM");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testFindExchangeRates_unknownCurrency() {
        ResponseEntity<Map> responseEntity =
                restTemplate.getForEntity("/currency-service/exchange-rates/from/{from}/type/{type}",
                        Map.class, "HMF", "BUY");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
