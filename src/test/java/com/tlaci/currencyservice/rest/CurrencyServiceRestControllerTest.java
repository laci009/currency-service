package com.tlaci.currencyservice.rest;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tlaci.currencyservice.rest.dto.ConversionResultDTO;
import com.tlaci.currencyservice.rest.dto.ExchangeRateDTO;
import com.tlaci.currencyservice.rest.error.ExchangeRateNotFoundException;
import com.tlaci.currencyservice.service.CurrencyService;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyServiceRestController.class)
public class CurrencyServiceRestControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")
    );

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyService currencyService;

    @Test
    public void testConvert_success() throws Exception {
        when(currencyService.convert(anyString(), anyString(),anyString(), any(BigDecimal.class)))
                .thenReturn(new ConversionResultDTO("USD", "HUF", "BUY", BigDecimal.valueOf(100), BigDecimal.valueOf(26377.200)));

        mockMvc.perform(get("/currency-service/convert/from/{from}/to/{to}/type/{type}/quantity/{quantity}", "USD", "HUF", "BUY", 100))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.from", is("USD")))
                .andExpect(jsonPath("$.to", is("HUF")))
                .andExpect(jsonPath("$.type", is("BUY")))
                .andExpect(jsonPath("$.quantity", comparesEqualTo(100)))
                .andExpect(jsonPath("$.amount", comparesEqualTo(26377.200)));
    }

    @Test
    public void testConvert_exchangeRateNotFound() throws Exception {
        when(currencyService.convert(anyString(), anyString(),anyString(), any(BigDecimal.class)))
                .thenThrow(new ExchangeRateNotFoundException("XYZ", "ZYX", "BUYSELL"));

        mockMvc.perform(get("/currency-service/convert/from/{from}/to/{to}/type/{type}/quantity/{quantity}", "HMM", "MMH", "BUY", 100))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testExchangeRates_success() throws Exception {
        List<ExchangeRateDTO> exchangeRates = Stream.of(
                new ExchangeRateDTO("HUF", "EUR","BUY", BigDecimal.valueOf(123.45)),
                new ExchangeRateDTO("HUF", "GBP","BUY", BigDecimal.valueOf(3121.33))).collect(Collectors.toList());
        when(currencyService.findExchangeRates(anyString(), anyString())).thenReturn(exchangeRates);

        mockMvc.perform(get("/currency-service/exchange-rates/from/{from}/type/{type}", "HUF", "SELL"))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2))).andReturn();
    }

    @Test
    public void testExchangeRates_exchangeRateNotFound() throws Exception {
        when(currencyService.findExchangeRates(anyString(), anyString()))
                .thenThrow(new ExchangeRateNotFoundException("XYZ", "ZYX"));

        mockMvc.perform(get("/currency-service/exchange-rates/from/{from}/type/{type}", "HMM", "SELL"))
                .andExpect(status().isNotFound());
    }
}
