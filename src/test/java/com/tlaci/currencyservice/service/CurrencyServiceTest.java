package com.tlaci.currencyservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tlaci.currencyservice.entity.ExchangeRate;
import com.tlaci.currencyservice.entity.ExchangeType;
import com.tlaci.currencyservice.repository.ExchangeRateRepository;
import com.tlaci.currencyservice.rest.dto.ConversionResultDTO;
import com.tlaci.currencyservice.rest.dto.ExchangeRateDTO;
import com.tlaci.currencyservice.rest.error.ExchangeRateNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceTest {

    @Mock
    private ExchangeRateRepository repository;
    @InjectMocks
    private CurrencyService currencyService;

    @Test
    public void testConvert_success() {
        when(repository.findByTypeAndFromAndTo(any(), anyString(), anyString()))
                .thenReturn(Optional.of(new ExchangeRate(ExchangeType.BUY, "GBP", "HUF", BigDecimal.valueOf(350))));

        ConversionResultDTO result = currencyService.convert("BUY", "GBP", "HUF", BigDecimal.valueOf(1000L));
        assertEquals("GBP", result.getFrom());
        assertEquals("HUF", result.getTo());
        assertEquals("BUY", result.getType());
        assertThat(BigDecimal.valueOf(1000L), Matchers.comparesEqualTo(result.getQuantity()));
        assertThat(BigDecimal.valueOf(350000L), Matchers.comparesEqualTo(result.getAmount()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_unknownExchangeType() {
        currencyService.convert("UNK", "GBP", "HUF", BigDecimal.valueOf(1000L));
    }

    @Test(expected = ExchangeRateNotFoundException.class)
    public void testConvert_unknownExchangeRate() {
        currencyService.convert("BUY", "MII", "HUF", BigDecimal.valueOf(1000L));
    }

    @Test
    public void testFindExchangeRates_success() {
        when(repository.findByTypeAndFrom(any(), anyString()))
                .thenReturn(Stream.of(
                        new ExchangeRate(ExchangeType.SELL, "GBP", "HUF", BigDecimal.valueOf(350)),
                        new ExchangeRate(ExchangeType.SELL, "GBP", "EUR", BigDecimal.valueOf(1.3))
                ).collect(Collectors.toList()));

        List<ExchangeRateDTO> exchangeRates = currencyService.findExchangeRates("SELL", "GBP");
        assertFalse(exchangeRates.isEmpty());
        assertEquals(2, exchangeRates.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindExchangeRates_unknownExchangeType() {
        currencyService.findExchangeRates("UNK", "GBP");
    }

    @Test(expected = ExchangeRateNotFoundException.class)
    public void testFindExchangeRates_unknownExchangeRate() {
        currencyService.findExchangeRates("SELL", "MII");
    }
}
