package com.tlaci.currencyservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlaci.currencyservice.entity.ExchangeRate;
import com.tlaci.currencyservice.entity.ExchangeType;
import com.tlaci.currencyservice.repository.ExchangeRateRepository;
import com.tlaci.currencyservice.rest.error.ExchangeRateNotFoundException;
import com.tlaci.currencyservice.rest.dto.ConversionResultDTO;
import com.tlaci.currencyservice.rest.dto.ExchangeRateDTO;

@Service
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public ConversionResultDTO convert(String type, String from, String to, BigDecimal quantity) {
        ExchangeType exchangeType = getExchangeType(type);
        final Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByTypeAndFromAndTo(exchangeType, from, to);
        if (!exchangeRate.isPresent()) {
            throw new ExchangeRateNotFoundException(from, to, type);
        }
        return convert(exchangeRate.get(), quantity);
    }

    public List<ExchangeRateDTO> findExchangeRates(String type, String from) {
        ExchangeType exchangeType = getExchangeType(type);
        final List<ExchangeRate> exchangeRates = exchangeRateRepository.findByTypeAndFrom(exchangeType, from);
        if (exchangeRates.isEmpty()) {
            throw new ExchangeRateNotFoundException(from, type);
        }
        return exchangeRates.stream().map(rate -> new ExchangeRateDTO(rate.getFrom(), rate.getTo(), rate.getType().name(),
                rate.getRate())).collect(Collectors.toList());
    }

    private ConversionResultDTO convert(ExchangeRate exchangeRate, BigDecimal quantity) {
        final BigDecimal convertedAmount = quantity.multiply(exchangeRate.getRate());
        LOGGER.info("Convert from {} to {} with type {} and quantity {}. Converted amount: {}", exchangeRate.getFrom(),
                exchangeRate.getTo(), exchangeRate.getType(), quantity, convertedAmount);
        return new ConversionResultDTO(exchangeRate.getFrom(), exchangeRate.getTo(), exchangeRate.getType().name(), quantity, convertedAmount);
    }

    private ExchangeType getExchangeType(String type) {
        try {
            return ExchangeType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid exchange type: " + type);
        }
    }
}
