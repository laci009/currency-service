package com.tlaci.currencyservice.rest.dto;

import java.math.BigDecimal;

public class ConversionResultDTO {

    private final String from;
    private final String to;
    private final String type;
    private final BigDecimal quantity;
    private final BigDecimal amount;

    public ConversionResultDTO(String from, String to, String type, BigDecimal quantity, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.quantity = quantity;
        this.amount = amount;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
