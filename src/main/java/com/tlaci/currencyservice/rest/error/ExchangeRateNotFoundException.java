package com.tlaci.currencyservice.rest.error;

public class ExchangeRateNotFoundException extends RuntimeException {

    private final String from;
    private final String to;
    private final String type;

    public ExchangeRateNotFoundException(String from, String type) {
        this(from, null, type);
    }

    public ExchangeRateNotFoundException(String from, String to, String type) {
        this.from = from;
        this.to = to;
        this.type = type;
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
}
