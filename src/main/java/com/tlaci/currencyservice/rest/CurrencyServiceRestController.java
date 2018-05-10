package com.tlaci.currencyservice.rest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tlaci.currencyservice.rest.dto.ConversionResultDTO;
import com.tlaci.currencyservice.rest.dto.ExchangeRateDTO;
import com.tlaci.currencyservice.service.CurrencyService;

@RestController
@RequestMapping("/currency-service")
public class CurrencyServiceRestController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/convert/from/{from}/to/{to}/type/{type}/quantity/{quantity}")
    public ConversionResultDTO convert(@PathVariable String from, @PathVariable String to, @PathVariable String type,
            @PathVariable BigDecimal quantity) {
        return currencyService.convert(type, from, to, quantity);
    }

    @GetMapping("/exchange-rates/from/{from}/type/{type}")
    public List<ExchangeRateDTO> findExchangeRates(@PathVariable String from, @PathVariable String type) {
        return currencyService.findExchangeRates(type, from);
    }
}
