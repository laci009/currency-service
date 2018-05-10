package com.tlaci.currencyservice.rest.dto;

import java.math.BigDecimal;

public class ExchangeRateDTO {

    private final String from;
    private final String to;
    private final String type;
    private final BigDecimal rate;

    public ExchangeRateDTO(String from, String to, String type, BigDecimal rate) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.rate = rate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
